package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.core;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.*;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.FileService;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.ShutterstockService;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class TransferAssetProcess {

    private final ReentrantLock lock = new ReentrantLock();
    private final ShutterstockService shutterstockService;
    private final FileService fileService;
    private final AtomicBoolean isInitialImportCompleted = new AtomicBoolean(false);

    public TransferAssetProcess(ShutterstockService shutterstockService, FileService fileService) {
        this.shutterstockService = shutterstockService;
        this.fileService = fileService;
    }

    @PostConstruct
    public void initialImport() {
        new Thread(() -> {
            log.info("Starting initial import of assets.");
            performInitialImport();
            isInitialImportCompleted.set(true);
            log.info("Initial import completed, starting scheduled tasks.");
        }).start();
    }

    private void performInitialImport() {
        int batchSize = 100;
        int page = 1;

//        // Process images for a single page
//        List<ImageDownloadDTO> allImages = getAllImages(page, batchSize);
//        processImages(allImages);
//
//        // Process videos for a single page
//        List<VideoDownloadDTO> allVideos = getAllVideos(page, batchSize);
//        processVideos(allVideos);
//
//        // Process audio for a single page
//        List<AudioDownloadDTO> allAudios = getAllAudios(page, batchSize);
//        processAudios(allAudios);

        List<ImageDownloadDTO> allImages;
        do {
            allImages = getAllImages(page, batchSize);
            processImages(allImages);
            page++;
        } while (!allImages.isEmpty());

        page = 1;
        List<VideoDownloadDTO> allVideos;
        do {
            allVideos = getAllVideos(page, batchSize);
            processVideos(allVideos);
            page++;
        } while (!allVideos.isEmpty());
    }

    @Scheduled(cron = "0 */15 * * * ?")
    public void uploadAssets() {
        if (!isInitialImportCompleted.get()) {
            log.info("Initial import not completed, skipping scheduled task.");
            return;
        }

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
                String tempImagePath = imageMetadataEn.getData().get(0).getOriginalFilename();

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
                    log.info("DownloadedVideoPath: {}", downloadedVideoPath);
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
        int batchSize = 50;
        List<ImageDownloadDTO> allImages;
        allImages = shutterstockService.getLicencedImagesByDate(page, batchSize, date, "images").getData();
        return allImages;
    }

    public List<VideoDownloadDTO> getAllVideosByDate(String date) {
        int page = 1;
        int batchSize = 20;
        List<VideoDownloadDTO> allVideos;
        allVideos = shutterstockService.getLicencedVideosByDate(page, batchSize, date, "videos").getData();
        return allVideos;
    }

    public List<AudioDownloadDTO> getAllAudiosByDate(String date) {
        int page = 1;
        int batchSize = 20;
        List<AudioDownloadDTO> allAudios;
        allAudios = shutterstockService.getLicencedAudiosByDate(page, batchSize, date, "audio").getData();
        return allAudios;
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
