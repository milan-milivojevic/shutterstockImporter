package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.ServerConfigurationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.*;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool.UploadMediaResult;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.utils.AssetUtils;
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
    private static final Pattern MEDIA_POOL_HASH_PATTERN = Pattern.compile("(?<=!\\s)[A-Za-z0-9+/=]+");

    public FileService(WebClient.Builder webClientBuilder, ServerConfigurationProperties properties, AssetService assetService) {
        this.assetService = assetService;
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

    public void loadVideoFileAndProcess(File filePath, VideoDownloadDTO video, ShutterstockVideoMetadataDto videoMetadataEn, ShutterstockVideoMetadataDto videoMetadataDe) {
        try {
            UploadMediaResult response = assetService.uploadFileToMediaPool(filePath);
            if (response.getError().isEmpty()) {
                log.info("resp: {}", response);
                final AssetMetadataTO assetMetadataTO = AssetUtils.computeVideoMetadata(response.getMediaGuid(), video, videoMetadataEn, videoMetadataDe);
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
}

