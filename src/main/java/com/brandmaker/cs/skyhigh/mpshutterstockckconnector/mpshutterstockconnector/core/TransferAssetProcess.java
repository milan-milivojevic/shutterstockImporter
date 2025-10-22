package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.core;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.ApplicationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.*;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.FileService;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.ShutterstockService;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.jobs.UpdateVectorImages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class TransferAssetProcess {

    private final ReentrantLock lock = new ReentrantLock();
    private final ShutterstockService shutterstockService;
    private final FileService fileService;
    private final ApplicationProperties applicationProperties;
    private final UpdateVectorImages updateVectorImages;
    private final CompletableFuture<Void> initialImportFuture = new CompletableFuture<>();
    private final CompletableFuture<Void> initialUpdateFuture = new CompletableFuture<>();
    private final CompletableFuture<Void> vectorUpdateFuture = new CompletableFuture<>();

    public TransferAssetProcess(ShutterstockService shutterstockService, FileService fileService, ApplicationProperties applicationProperties, UpdateVectorImages updateVectorImages) {
        this.shutterstockService = shutterstockService;
        this.fileService = fileService;
        this.applicationProperties = applicationProperties;
        this.updateVectorImages = updateVectorImages;
    }

    @PostConstruct
    public void initialImport() {
        if (!applicationProperties.isRunInitialImport()) {
            log.info("Initial import is disabled by configuration (run-initial-import=false).");
            initialImportFuture.complete(null);
            return;
        }

        CompletableFuture
          .runAsync(() -> {
              log.info("Starting initial import of assets because run-initial-import=true.");
              performInitialImport();
          })
          .whenComplete((ignored, throwable) -> {
              if (throwable != null) {
                  Throwable cause = unwrap(throwable);
                  log.error("Initial import failed: {}", cause.getMessage(), cause);
              } else {
                  log.info("Initial import completed.");
              }
              initialImportFuture.complete(null);
          });
    }

    private void performInitialImport() {
        final int imageBatchSize = 100;
        final int videoBatchSize = 50;
        final int audioBatchSize = 50;

        for (int page = 1; ; page++) {
            try {
                List<ImageDownloadDTO> allImages = getAllImages(page, imageBatchSize);
                if (allImages.isEmpty()) {
                    break;
                }
                processImages(allImages);
            } catch (Exception e) {
                log.error("Error fetching images on page {} -> {}. Skipping this page.", page, e.getMessage());
            }
        }

        for (int page = 1; ; page++) {
            try {
                List<VideoDownloadDTO> allVideos = getAllVideos(page, videoBatchSize);
                if (allVideos.isEmpty()) {
                    break;
                }
                processVideos(allVideos);
            } catch (Exception e) {
                log.error("Error fetching videos on page {} -> {}. Skipping this page.", page, e.getMessage());
            }
        }

        for (int page = 1; ; page++) {
            try {
                List<AudioDownloadDTO> allAudios = getAllAudios(page, audioBatchSize);
                if (allAudios.isEmpty()) {
                    break;
                }
                processAudios(allAudios);
            } catch (Exception e) {
                log.error("Error fetching audios on page {} -> {}. Skipping this page.", page, e.getMessage());
            }
        }

//        int batchSize = 1;
//        int page = 1748;

        // Process images for a single page
//        List<ImageDownloadDTO> allImages = getAllImages(page, batchSize);
//        processImages(allImages);

        // Process videos for a single page
//        List<VideoDownloadDTO> allVideos = getAllVideos(page, batchSize);
//        processVideos(allVideos);

        // Process audio for a single page
//        List<AudioDownloadDTO> allAudios = getAllAudios(page, batchSize);
//        processAudios(allAudios);
    }

    @PostConstruct
    public void initialUpdate() {
        if (!applicationProperties.isRunInitialUpdate()) {
            log.info("Initial update is disabled by configuration (run-initial-update=false).");
            initialUpdateFuture.complete(null);
            return;
        }

        log.info("Initial update will start once the initial import has completed.");
        initialImportFuture
          .thenRunAsync(() -> {
              log.info("Starting initial update of existing assets because run-initial-update=true.");
              performInitialUpdate();
          })
          .whenComplete((ignored, throwable) -> {
              if (throwable != null) {
                  Throwable cause = unwrap(throwable);
                  log.error("Initial update failed: {}", cause.getMessage(), cause);
              } else {
                  log.info("Initial update completed.");
              }
              initialUpdateFuture.complete(null);
          });
    }

    private void performInitialUpdate() {
        final int batchSize = 100;
        for (int page = 1; ; page++) {
            try {
                List<ImageDownloadDTO> images = getAllImages(page, batchSize);
                if (images.isEmpty()) {
                    break;
                }
                processExistingImages(images);
            } catch (Exception e) {
                log.error("Error fetching images on page {} -> {}. Skipping this page.", page, e.getMessage());
            }
        }
    }

    @PostConstruct
    public void runVectorImagesUpdate() {
        if (!applicationProperties.isRunVectorImagesUpdate()) {
            log.info("Vector images update is disabled by configuration (run-vector-images-update=false).");
            vectorUpdateFuture.complete(null);
            return;
        }
        log.info("Vector images update will start once initial import and initial update have completed.");
        CompletableFuture
          .allOf(initialImportFuture, initialUpdateFuture)
          .thenRunAsync(() -> {
              log.info("Starting vector images update because run-vector-images-update=true.");
              updateVectorImages.run();
          })
          .whenComplete((ignored, throwable) -> {
              if (throwable != null) {
                  Throwable cause = unwrap(throwable);
                  log.error("Vector images update failed: {}", cause.getMessage(), cause);
              } else {
                  log.info("Vector images update completed.");
              }
              vectorUpdateFuture.complete(null);
          });
    }

    @Scheduled(cron = "0 */15 * * * ?")
    public void uploadAssets() {
        if (!initialImportFuture.isDone()) {
            log.info("Initial import not completed, skipping scheduled task.");
            return;
        }

        if (!initialUpdateFuture.isDone()) {
            log.info("Initial update not completed, skipping scheduled task.");
            return;
        }

        if (!vectorUpdateFuture.isDone()) {
            log.info("Vector images update not completed, skipping scheduled task.");
            return;
        }

        log.info("Starting scheduled tasks.");
        if (lock.tryLock()) {
            try {
                String date = getCurrentTimeMinus15Minutes();
                List<ImageDownloadDTO> allImagesByDate = getAllImagesByDate(date);
                if (allImagesByDate.isEmpty()) {
                    log.info("There are no images to download.");
                }
                processImages(allImagesByDate);

                List<VideoDownloadDTO> allVideosByDate = getAllVideosByDate(date);
                if (allVideosByDate.isEmpty()) {
                    log.info("There are no videos to download.");
                }
                processVideos(allVideosByDate);

                List<AudioDownloadDTO> allAudiosByDate = getAllAudiosByDate(date);
                if (allAudiosByDate.isEmpty()) {
                    log.info("There are no audios to download.");
                }
                processVideos(allVideosByDate);
            } catch (Exception e) {
                log.error("Error during scheduled task: ", e);
            } finally {
                lock.unlock();
            }
        } else {
            log.info("Previous process is still running, skipping this scheduled run.");
        }
    }

    private void processImages(List<ImageDownloadDTO> allImages) {
        for (ImageDownloadDTO item : allImages) {
            try {
                LincensedImageDto licensedImage = shutterstockService.licensedImage(item.getId(), "images");
                log.info(licensedImage.getUrl());
                log.info(item.getImage().getId().replaceAll("\"", ""));
                Long imageId = Long.parseLong(item.getImage().getId());
                log.info("Image id: " + imageId);
                ShutterstockImageMetadataDto imageMetadataEn = shutterstockService.getMetadata(imageId, "images", "en");
                if (imageMetadataEn.getData().isEmpty())
                    continue;
                ShutterstockImageMetadataDto imageMetadataDe = shutterstockService.getMetadata(imageId, "images", "de");
                if (imageMetadataDe.getData().isEmpty())
                    continue;
                log.info(imageMetadataEn.getData().get(0).getOriginalFilename());

                String imageUrl = licensedImage.getUrl();

                String id = imageMetadataEn.getData().get(0).getId();
                String type = imageMetadataEn.getData().get(0).getImageType();

                String tempImagePath;
                if ("vector".equalsIgnoreCase(type)) {
                    tempImagePath = id + ".eps";
                } else {
                    tempImagePath = id + ".jpg";
                }


                try {
                    File downloadedImagePath = fileService.downloadImage(imageUrl, tempImagePath);
                    log.info("DownloadedImagePath: {}", downloadedImagePath);
                    fileService.loadFileAndProcess(downloadedImagePath, item, imageMetadataEn, imageMetadataDe);
                } catch (IOException e) {
                    log.error("Failed to download image: {}", e.getMessage());
                } finally {
                    // Clean up temporary file
                    File tempFile = new File(tempImagePath);
                    if (tempFile.exists()) {
                        if (!tempFile.delete()) {
                            log.warn("Failed to delete temporary file: {}", tempImagePath);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error processing image with ID {}: {}", item.getId(), e.getMessage());
            }
        }
    }

    private void processExistingImages(List<ImageDownloadDTO> allImages) {
        for (ImageDownloadDTO item : allImages) {
            try {
                Long imageId = Long.parseLong(item.getImage().getId());
                log.info("Image id: " + imageId);
                ShutterstockImageMetadataDto imageMetadataEn = shutterstockService.getMetadata(imageId, "images", "en");
                if (imageMetadataEn.getData().isEmpty())
                    continue;
                ShutterstockImageMetadataDto imageMetadataDe = shutterstockService.getMetadata(imageId, "images", "de");
                if (imageMetadataDe.getData().isEmpty())
                    continue;

                log.info(imageMetadataEn.getData().get(0).getOriginalFilename());

                fileService.updateExistingAssets(item, imageMetadataEn, imageMetadataDe);

            } catch (Exception e) {
                log.error("Error while updating image with ID {}: {}", item.getId(), e.getMessage());
            }
        }
    }

    private void processVideos(List<VideoDownloadDTO> allVideos) {
        for (VideoDownloadDTO item : allVideos) {
            try {
                LincensedVideoDto licensedVideo = shutterstockService.licensedVideo(item.getId(), "videos");
                log.info(licensedVideo.getUrl());
                log.info(item.getVideo().getId().replaceAll("\"", ""));
                Long videoId = Long.parseLong(item.getVideo().getId());
                log.info("Video id: " + videoId);
                ShutterstockVideoMetadataDto videoMetadataEn = shutterstockService.getVideoMetadata(videoId, "videos", "en");
                if (videoMetadataEn.getData().isEmpty())
                    continue;
                ShutterstockVideoMetadataDto videoMetadataDe = shutterstockService.getVideoMetadata(videoId, "videos", "de");
                if (videoMetadataDe.getData().isEmpty())
                    continue;
                log.info(videoMetadataEn.getData().get(0).getOriginalFilename());

                String videoUrl = licensedVideo.getUrl();
                String tempVideoPath = videoMetadataEn.getData().get(0).getOriginalFilename();

                try {
                    File downloadedVideoPath = fileService.downloadImage(videoUrl, tempVideoPath);
                    log.info("downloadedVideoPath: {}", downloadedVideoPath);
                    fileService.loadVideoFileAndProcess(downloadedVideoPath, item, videoMetadataEn, videoMetadataDe);
                } catch (IOException e) {
                    log.error("Failed to download video: {}", e.getMessage());
                } finally {
                    File tempFile = new File(tempVideoPath);
                    if (tempFile.exists()) {
                        if (!tempFile.delete()) {
                            log.warn("Failed to delete temporary file: {}", tempVideoPath);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error processing video with ID {}: {}", item.getId(), e.getMessage());
            }
        }
    }

    private void processAudios(List<AudioDownloadDTO> allAudios) {
        for (AudioDownloadDTO item : allAudios) {
            try {
                LincensedAudioDto licensedAudio = shutterstockService.licensedAudio(item.getId(), "audio");
                log.info(licensedAudio.getUrl());
                log.info(item. getAudio().getId().replaceAll("\"", ""));
                Long audioId = Long.parseLong(item.getAudio().getId());
                log.info("Audio id: " + audioId);
                ShutterstockAudioMetadataDto audioMetadataEn = shutterstockService.getAudioMetadata(audioId, "audio", "en");
                if (audioMetadataEn.getData().isEmpty())
                    continue;
                ShutterstockAudioMetadataDto audioMetadataDe = shutterstockService.getAudioMetadata(audioId, "audio", "de");
                if (audioMetadataDe.getData().isEmpty())
                    continue;
                log.info(audioMetadataEn.getData().get(0).getTitle());

                String audioUrl = licensedAudio.getUrl();
                String tempAudioPath = audioMetadataEn.getData().get(0).getTitle();

                try {
                    File downloadedAudioPath = fileService.downloadImage(audioUrl, tempAudioPath);
                    log.info("DownloadedAudioPath: {}", downloadedAudioPath);
                    fileService.loadAudioFileAndProcess(downloadedAudioPath, item, audioMetadataEn, audioMetadataDe);
                } catch (IOException e) {
                    log.error("Failed to download audio: {}", e.getMessage());
                } finally {
                    File tempFile = new File(tempAudioPath);
                    if (tempFile.exists()) {
                        if (!tempFile.delete()) {
                            log.warn("Failed to delete temporary file: {}", tempAudioPath);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error processing audio with ID {}: {}", item.getId(), e.getMessage());
            }
        }
    }

    public List<ImageDownloadDTO> getAllImages(int page, int batchSize) {
        List<ImageDownloadDTO> allImages;
        allImages = shutterstockService.getLicencedImages(page, batchSize, "images").getData();
        return allImages;
    }

    public List<VideoDownloadDTO> getAllVideos(int page, int batchSize) {
        List<VideoDownloadDTO> allVideos;
        allVideos = shutterstockService.getLicencedVideo(page, batchSize, "videos").getData();
        return allVideos;
    }

    public List<AudioDownloadDTO> getAllAudios(int page, int batchSize) {
        List<AudioDownloadDTO> allAudios;
        allAudios = shutterstockService.getLicencedAudio(page, batchSize, "audio").getData();
        return allAudios;
    }

    public List<ImageDownloadDTO> getAllImagesByDate(String date) {
        int page = 1;
        int batchSize = 100;
        List<ImageDownloadDTO> allImages;
        allImages = shutterstockService.getLicencedImagesByDate(page, batchSize, date, "images").getData();
        return allImages;
    }

    public List<VideoDownloadDTO> getAllVideosByDate(String date) {
        int page = 1;
        int batchSize = 50;
        List<VideoDownloadDTO> allVideos;
        allVideos = shutterstockService.getLicencedVideosByDate(page, batchSize, date, "videos").getData();
        return allVideos;
    }

    public List<AudioDownloadDTO> getAllAudiosByDate(String date) {
        int page = 1;
        int batchSize = 50;
        List<AudioDownloadDTO> allAudios;
        allAudios = shutterstockService.getLicencedAudiosByDate(page, batchSize, date, "audio").getData();
        return allAudios;
    }

    private Throwable unwrap(Throwable throwable) {
        if (throwable instanceof CompletionException && throwable.getCause() != null) {
            return throwable.getCause();
        }
        return throwable;
    }

//    public static String getCurrentTimeMinus15Minutes() {
//        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("CET"));
//        LocalDateTime timeMinus15Minutes = currentTime.minusMinutes(15);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        return timeMinus15Minutes.format(formatter);
//    }

    public static String getCurrentTimeMinus15Minutes() {
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime timeMinus15Minutes = currentTime.minusMinutes(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        log.info(timeMinus15Minutes.format(formatter));
        return timeMinus15Minutes.format(formatter);
    }
}
