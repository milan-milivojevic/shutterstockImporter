package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.*;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool.UploadMediaResult;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.utils.AssetUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class FileService {
    private final WebClient webClient;
    private final AssetService assetService;
    private final MediaPoolSearchPayloadFactory searchPayloadFactory;
    private static final Pattern MEDIA_POOL_HASH_PATTERN = Pattern.compile("(?<=!\\s)[A-Za-z0-9+/=]+");

    public FileService(WebClient.Builder webClientBuilder, AssetService assetService, MediaPoolSearchPayloadFactory searchPayloadFactory) {
        this.assetService = assetService;
        this.searchPayloadFactory = searchPayloadFactory;
        this.webClient = webClientBuilder.build();

    }

    public static File downloadImage(String imageUrl, String localFilePath) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(imageUrl);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try (FileOutputStream outputStream = new FileOutputStream(localFilePath)) {
                    byte[] buffer = new byte[8 * 1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } finally {
            response.close();
            httpClient.close();
        }
        return new File(localFilePath);
    }

    public void loadFileAndProcess(File filePath, ImageDownloadDTO image, ShutterstockImageMetadataDto imageMetadataEn, ShutterstockImageMetadataDto imageMetadataDe) throws IOException {
        try {
            UploadMediaResult response = assetService.uploadFileToMediaPool(filePath);
            if (response.getError().isEmpty()) {
                log.info("resp: {}", response);
                final AssetMetadataTO assetMetadataTO = AssetUtils.computeAssetMetadata(response.getMediaGuid(), image, imageMetadataEn, imageMetadataDe);
                this.assetService.updateAssetMetadata(assetMetadataTO);

            } else if (response.getError().contains("The media already exists!")) {

                final Matcher matcher = MEDIA_POOL_HASH_PATTERN.matcher(response.getError());

                if (matcher.find()) {
                    final String hashCode = matcher.group();
                    final String mediaGuid = this.assetService.getAssetIdByHashCode(hashCode);
                    response.setMediaGuid(mediaGuid);
                }
            } else {
                log.error("Cannot upload file. Error: {}", response.getError());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void updateExistingAssets(ImageDownloadDTO image, ShutterstockImageMetadataDto imageMetadataEn, ShutterstockImageMetadataDto imageMetadataDe) throws IOException {
        try {
            String shutterstockId = image.getImage().getId();
            String stockValue = "STOCK " + shutterstockId;
            AssetSearchTO searchTO = searchPayloadFactory.buildUpdateExistingAssetsPayload(stockValue);
            log.info("Searching asset for Shutterstock ID={} using stock title …", shutterstockId);
            log.info("Search payload: {}", searchTO);

            AssetSearchResponseTO response = assetService.searchAssets(searchTO);
            int totalHits = response == null ? 0 : response.getTotalHits();
            log.info("Search finished. totalHits={}", totalHits);

            if (response == null || totalHits <= 0 || response.getItems().isEmpty()) {
                log.warn("Cannot update asset. No Media Pool entry found for stock title '{}'.", stockValue);
                return;
            }

            String assetId = null;
            for (JsonNode item : response.getItems()) {
                if (item == null) {
                    continue;
                }
                boolean vectorOfficial = MediaPoolService.isVectorOfficial(item);
                if (vectorOfficial) {
                    log.debug("Skipping assetId={} because it already has an official vector version.",
                      MediaPoolService.readAssetId(item));
                    continue;
                }
                assetId = extractAssetId(item);
                if (assetId != null && !assetId.isBlank()) {
                    break;
                }
            }

            if (assetId == null || assetId.isBlank()) {
                log.info("All hits already have official vector versions for stock '{}'. Skipping title update.",
                  stockValue);
                return;
            }

            final AssetMetadataTO assetMetadataTO =
              AssetUtils.computeExistingAssetMetadata(assetId, image, imageMetadataEn, imageMetadataDe);
            this.assetService.updateAssetMetadata(assetMetadataTO);
        } catch (Exception e) {
            log.error("Error in updateExistingAssets: {}", e.getMessage(), e);
        }
    }


    public void loadVideoFileAndProcess(File filePath, VideoDownloadDTO video, ShutterstockVideoMetadataDto videoMetadataEn, ShutterstockVideoMetadataDto videoMetadataDe) {
        try {
            UploadMediaResult response = assetService.uploadFileToMediaPool(filePath);
            if (response.getError().isEmpty()) {
                log.info("resp: {}", response);
                final AssetMetadataTO assetMetadataTO = AssetUtils.computeVideoMetadata(response.getMediaGuid(), video, videoMetadataEn, videoMetadataDe);
                this.assetService.updateAssetMetadata(assetMetadataTO);

            } else if (response.getError().contains("The media already exists!")) {
                log.info("response: {}", response.getError());
                final Matcher matcher = MEDIA_POOL_HASH_PATTERN.matcher(response.getError());

                if (matcher.find()) {
                    final String hashCode = matcher.group();
                    final String mediaGuid = this.assetService.getAssetIdByHashCode(hashCode);
                    response.setMediaGuid(mediaGuid);
                }
            } else {
                log.error("Cannot upload file. Error: {}", response.getError());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void loadAudioFileAndProcess(File filePath, AudioDownloadDTO audio, ShutterstockAudioMetadataDto audioMetadataEn, ShutterstockAudioMetadataDto audioMetadataDe) {
        try {
            UploadMediaResult response = assetService.uploadFileToMediaPool(filePath);
            if (response.getError().isEmpty()) {
                log.info("resp: {}", response);
                final AssetMetadataTO assetMetadataTO = AssetUtils.computeAudioMetadata(response.getMediaGuid(), audio, audioMetadataEn, audioMetadataDe);
                this.assetService.updateAssetMetadata(assetMetadataTO);

            } else if (response.getError().contains("The media already exists!")) {
                log.info("response: {}", response);
                final Matcher matcher = MEDIA_POOL_HASH_PATTERN.matcher(response.getError());

                if (matcher.find()) {
                    final String hashCode = matcher.group();
                    final String mediaGuid = this.assetService.getAssetIdByHashCode(hashCode);
                    response.setMediaGuid(mediaGuid);
                }
            } else {
                log.error("Cannot upload file. Error: {}", response.getError());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private String extractAssetId(JsonNode item) {
        if (item == null) {
            return null;
        }
        JsonNode versionItems = item.path("fields").path("versions").path("items");
        if (versionItems.isArray() && versionItems.size() > 0) {
            JsonNode idNode = versionItems.get(0)
              .path("fields")
              .path("assetId")
              .path("value");
            if (!idNode.isMissingNode() && !idNode.isNull()) {
                return idNode.asText();
            }
        }
        return null;
    }
}