package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.jobs;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.ApplicationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.MediaPoolService;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.ShutterstockService;

import java.util.concurrent.atomic.AtomicBoolean;

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
					withRetryVoid(3, () -> {
						mediaPoolService.uploadAssetVersion(assetId, "Update as Dummy", dummyEps, "dummy.eps", MediaType.valueOf("application/postscript"));
					}, "[UVI-7] upload dummy failed, attempt=%d");
					Long dummyVersion = findVersionByComment(assetId, "Update as Dummy");
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

					withRetryVoid(3, () -> {
						mediaPoolService.uploadAssetVersion(assetId, "Update as EPS", epsResource, "shutterstock_" + shutterId + ".eps", MediaType.valueOf("application/postscript"));
					}, "[UVI-9] upload EPS failed, attempt=%d");

					Long epsVersion = findVersionByComment(assetId, "Update as EPS");
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

	private void withRetryVoid(int times, Runnable action, String failLogFmt) {
		for (int i = 1; i <= times; i++) {
			try {
				action.run();
				return;
			} catch (Exception e) {
				log.warn(failLogFmt, i, e);
				if (i == times) throw e;
				try { Thread.sleep(i * 500L); } catch (InterruptedException ignored) {}
			}
		}
	}

	private Long findVersionByComment(long assetId, String comment) {
		JsonNode versions = mediaPoolService.getAssetVersions(assetId);
		JsonNode items = versions.path("items");
		if (!items.isArray()) return null;
		for (JsonNode v : items) {
			String c = v.at("/fields/uploadComments/value").asText("");
			long vn = v.at("/fields/versionNumber/value").asLong(-1);
			if (comment.equals(c)) return vn;
		}
		return null;
	}

	private void deleteAllBut(long assetId, long keepVersion) {
		JsonNode versions = mediaPoolService.getAssetVersions(assetId);
		JsonNode items = versions.path("items");
		if (!items.isArray()) return;
		for (JsonNode v : items) {
			long vn = v.at("/fields/versionNumber/value").asLong(-1);
			if (vn >= 0 && vn != keepVersion) {
				try {
					mediaPoolService.removeVersion(assetId, vn);
				} catch (Exception e) {
					log.warn("[UVI-DEL] Cannot remove version={} assetId={}, continue. {}", vn, assetId, e.getMessage());
				}
			}
		}
	}

	private byte[] downloadBytes(String url) {
		try {
			return org.springframework.web.reactive.function.client.WebClient.create()
				.get().uri(url)
				.retrieve()
				.bodyToMono(byte[].class)
				.block();
		} catch (Exception e) {
			log.warn("[UVI-DL] Download failed: {}", e.getMessage());
			return null;
		}
	}
}
