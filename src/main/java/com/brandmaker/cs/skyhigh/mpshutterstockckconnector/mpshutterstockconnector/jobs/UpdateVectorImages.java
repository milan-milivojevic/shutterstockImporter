package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.ApplicationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.MediaPoolService;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.ShutterstockService;

import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateVectorImages {

	private static final AtomicBoolean ALREADY_RAN = new AtomicBoolean(false);

	private final ApplicationProperties props;
	private final ShutterstockService shutterstockService;
	private final MediaPoolService mediaPoolService;

	@Value("classpath:vector/dummy.eps")
	private Resource dummyEps;

	public void run() {
		if (!props.isRunVectorImagesUpdate()) {
			log.info("[UVI-0] run_vector_images_update=false → skip.");
			return;
		}
		if (!ALREADY_RAN.compareAndSet(false, true)) {
			log.info("[UVI-0] UpdateVectorImages already ran in this runtime → skip.");
			return;
		}

		final int perPage = 100;
		int page = 1;
		int processed = 0;

		log.info("[UVI-1] Starting vector update: perPage={}, sequential=true", perPage);

		while (true) {
			JsonNode licenses = shutterstockService.getLicencedImagesRaw(page, perPage, "images");
			JsonNode data = licenses.path("data");
			if (!data.isArray() || data.size() == 0) {
				log.info("[UVI-2] No more data at page={} → done. processed={}", page, processed);
				break;
			}

			for (JsonNode lic : data) {
				try {
					String size = lic.at("/image/format/size").asText("");
					if (!"vector".equalsIgnoreCase(size)) {
						continue;
					}

					String licenseId = lic.path("id").asText(null);
					String shutterId = lic.at("/image/id").asText(null);
					if (licenseId == null || shutterId == null) {
						log.warn("[UVI-3] Missing licenseId or image.id, skipping. node={}", lic);
						continue;
					}

					processed++;
					String stockValue = "STOCK " + shutterId;
					log.info("[UVI-4] [{}] Processing vector shutterstockId={}, licenseId={}", processed, shutterId, licenseId);

					JsonNode searchResp = mediaPoolService.searchByStockTitleValue(stockValue);
					int totalHits = searchResp.path("totalHits").asInt(0);
					if (totalHits <= 0) {
						log.warn("[UVI-5] [{}] MP search no hits for '{}', skip.", processed, stockValue);
						continue;
					}

					JsonNode items = searchResp.path("items");
					if (!items.isArray() || items.size() == 0) {
						log.warn("[UVI-5b] [{}] MP search items empty for '{}', skip.", processed, stockValue);
						continue;
					}

					Long assetIdToFix = null;
					for (int i = 0; i < items.size(); i++) {
						JsonNode item = items.get(i);
						long assetId = MediaPoolService.readAssetId(item);
						boolean vectorOfficial = MediaPoolService.isVectorOfficial(item);
						if (!vectorOfficial) {
							assetIdToFix = assetId;
							break;
						}
					}
					if (assetIdToFix == null) {
						log.info("[UVI-6] [{}] All hits already vector official for '{}', skip.", processed, stockValue);
						continue;
					}

					final long assetId = assetIdToFix;

					try {
						mediaPoolService.uploadAssetVersion(assetId, "Update as Dummy", dummyEps, "dummy.eps", MediaType.valueOf("application/postscript"));
					} catch (WebClientResponseException e) {
						log.warn("[UVI-7] upload dummy failed status={} body={}", e.getStatusCode().value(), safeBody(e));
						if (e.getStatusCode() != HttpStatus.CONFLICT) {
							throw e;
						}
					}

					Long dummyVersion = waitForVersion(assetId, "Update as Dummy", "dummy.eps", 12, 1000);
					if (dummyVersion == null) {
						log.warn("[UVI-7b] [{}] Cannot find 'Update as Dummy' version on assetId={}, skip to next.", processed, assetId);
						continue;
					}

					mediaPoolService.setOfficialVersion(assetId, dummyVersion);
					deleteAllBut(assetId, dummyVersion);

					String gatekeeperUrl = shutterstockService.requestImageDownloadUrl(licenseId, "images");
					if (gatekeeperUrl == null || gatekeeperUrl.isBlank()) {
						log.warn("[UVI-8] [{}] No EPS URL for licenseId={}, skip.", processed, licenseId);
						continue;
					}

					byte[] epsBytes = downloadBytes(gatekeeperUrl);
					if (epsBytes == null || epsBytes.length == 0) {
						gatekeeperUrl = shutterstockService.requestImageDownloadUrl(licenseId, "images");
						epsBytes = downloadBytes(gatekeeperUrl);
						if (epsBytes == null || epsBytes.length == 0) {
							log.warn("[UVI-8b] [{}] Cannot download EPS for licenseId={}, skip.", processed, licenseId);
							continue;
						}
					}

					Resource epsResource = new ByteArrayResource(epsBytes) {
						@Override public String getFilename() { return "shutterstock_" + shutterId + ".eps"; }
					};

					try {
						mediaPoolService.uploadAssetVersion(assetId, "Update as EPS", epsResource, "shutterstock_" + shutterId + ".eps", MediaType.valueOf("application/postscript"));
					} catch (WebClientResponseException e) {
						log.warn("[UVI-9] upload EPS failed status={} body={}", e.getStatusCode().value(), safeBody(e));
						if (e.getStatusCode() != HttpStatus.CONFLICT) {
							throw e;
						}
					}

					Long epsVersion = waitForVersion(assetId, "Update as EPS", "shutterstock_" + shutterId + ".eps", 18, 1000);
					if (epsVersion == null) {
						log.warn("[UVI-9b] [{}] Cannot find 'Update as EPS' version on assetId={}, skip cleanup.", processed, assetId);
						continue;
					}

					mediaPoolService.setOfficialVersion(assetId, epsVersion);
					mediaPoolService.removeVersion(assetId, dummyVersion);

					log.info("[UVI-10] [{}] OK assetId={} → EPS official set.", processed, assetId);

				} catch (Exception ex) {
					log.error("[UVI-E] Unexpected error, continue with next. msg={}", ex.getMessage(), ex);
				}
			}

			page++;
		}

		log.info("[UVI-END] Completed vector update. processed={} items.", processed);
	}

	private String safeBody(WebClientResponseException e) {
		try { return e.getResponseBodyAsString(); } catch (Exception ignore) { return ""; }
	}

	private Long waitForVersion(long assetId, String comment, String filename, int attempts, long sleepMs) {
		for (int i = 0; i < attempts; i++) {
			Long v = findVersionByCommentOrFilename(assetId, comment, filename);
			if (v != null) return v;
			try { Thread.sleep(sleepMs); } catch (InterruptedException ignored) {}
		}
		return null;
	}

	private Long findVersionByCommentOrFilename(long assetId, String comment, String filename) {
		JsonNode versions = mediaPoolService.getAssetVersions(assetId);

		if (!versions.isArray()) {
			log.warn("Unexpected MP versions payload (expected array). payload={}", versions);
			return null;
		}

		for (JsonNode v : versions) {
			String c  = v.path("uploadComments").asText(null);
			String fn = v.path("fileResource").path("fileName").asText(null);
			long vn   = v.path("versionNumber").asLong(-1);

			if (vn < 0) continue;
			if (comment != null && comment.equals(c)) return vn;
			if (filename != null && fn != null && filename.equalsIgnoreCase(fn)) return vn;
		}
		return null;
	}


	private void deleteAllBut(long assetId, long keepVersion) {
		JsonNode versions = mediaPoolService.getAssetVersions(assetId);
		if (!versions.isArray()) {
			log.warn("Unexpected MP versions payload (expected array). payload={}", versions);
			return;
		}
		for (JsonNode v : versions) {
			long vn = v.path("versionNumber").asLong(-1);
			if (vn >= 0 && vn != keepVersion) {
				try {
					mediaPoolService.removeVersion(assetId, vn);
				} catch (Exception e) {
					log.warn("[UVI-DEL] Cannot remove version={} assetId={}, continue. {}", vn, assetId, e.getMessage());
				}
			}
		}
	}

	private static final WebClient DL_CLIENT = WebClient.builder()
		.exchangeStrategies(ExchangeStrategies.builder()
			.codecs(c -> c.defaultCodecs().maxInMemorySize(64 * 1024 * 1024)) // 64MB
			.build())
		.build();

	private byte[] downloadBytes(String url) {
		try {
			return DL_CLIENT.get()
				.uri(url)
				.retrieve()
				.bodyToMono(byte[].class)
				.block();
		} catch (Exception e) {
			log.warn("[UVI-DL] Download failed: {}", e.getMessage());
			return null;
		}
	}
}
