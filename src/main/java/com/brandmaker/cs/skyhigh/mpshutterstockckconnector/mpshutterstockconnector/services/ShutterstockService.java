package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants.Endpoints;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.*;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.helpers.WebClientShutterstockHelper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ShutterstockService {
    private final WebClientShutterstockHelper webClientHelper;

    public ShutterstockService(WebClientShutterstockHelper webClientHelper) {
        this.webClientHelper = webClientHelper;
    }

    public ImageDownloadResponseDTO getLicencedImages(Integer page, Integer perPage, String assetType) {
        String path = String.format(Endpoints.GET_IMAGES, assetType, page, perPage);
        return webClientHelper.sendGetRequest(path, ImageDownloadResponseDTO.class, Object.class).getBody();
    }

    public ImageDownloadResponseDTO getLicencedImagesByDate(Integer page, Integer perPage, String date, String assetType) {
        String path = String.format(Endpoints.GET_IMAGES_BY_DATE, assetType, page, perPage, date);
        return webClientHelper.sendGetRequest(path, ImageDownloadResponseDTO.class, Object.class).getBody();
    }

    public LincensedImageDto licensedImage(String id, String assetType) {
        String emptyJson = "{}";
        return webClientHelper.sendPostRequest(String.format(Endpoints.LICENSED_IMAGE, assetType, id), emptyJson, LincensedImageDto.class).getBody();
    }

    public ShutterstockImageMetadataDto getMetadata(Long id, String assetType, String language) {
        String path = String.format(Endpoints.GET_METADATA, assetType, id, language);
        return webClientHelper.sendGetRequest(path, ShutterstockImageMetadataDto.class).getBody();
    }

    public VideoDownloadResponseDTO getLicencedVideo(Integer page, Integer perPage, String assetType) {
        String path = String.format(Endpoints.GET_IMAGES, assetType, page, perPage);
        return webClientHelper.sendGetRequest(path, VideoDownloadResponseDTO.class, Object.class).getBody();
    }

    public VideoDownloadResponseDTO getLicencedVideosByDate(Integer page, Integer perPage, String date, String assetType) {
        String path = String.format(Endpoints.GET_IMAGES_BY_DATE, assetType, page, perPage, date);
        return webClientHelper.sendGetRequest(path, VideoDownloadResponseDTO.class, Object.class).getBody();
    }

    public LincensedVideoDto licensedVideo(String id, String assetType) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("size", "web");

        String path = String.format(Endpoints.LICENSED_IMAGE, assetType, id);
        log.info("Path: " + path);
        return webClientHelper.sendPostRequest(path, requestBodyMap, LincensedVideoDto.class).getBody();
    }

    public ShutterstockVideoMetadataDto getVideoMetadata(Long id, String assetType, String language) {
        String path = String.format(Endpoints.GET_METADATA, assetType, id, language);
        log.info(path);
        return webClientHelper.sendGetRequest(path, ShutterstockVideoMetadataDto.class).getBody();
    }

    public AudioDownloadResponseDTO getLicencedAudio(Integer page, Integer perPage, String assetType) {
        String path = String.format(Endpoints.GET_IMAGES, assetType, page, perPage);
        return webClientHelper.sendGetRequest(path, AudioDownloadResponseDTO.class, Object.class).getBody();
    }

    public AudioDownloadResponseDTO getLicencedAudiosByDate(Integer page, Integer perPage, String date, String assetType) {
        String path = String.format(Endpoints.GET_IMAGES_BY_DATE, assetType, page, perPage, date);
        return webClientHelper.sendGetRequest(path, AudioDownloadResponseDTO.class, Object.class).getBody();
    }

    public LincensedAudioDto licensedAudio(String id, String assetType) {
        String emptyJson = "{}";
        return webClientHelper.sendPostRequest(String.format(Endpoints.LICENSED_IMAGE, assetType, id), emptyJson, LincensedAudioDto.class).getBody();
    }

    public ShutterstockAudioMetadataDto getAudioMetadata(Long id, String assetType, String language) {
        String path = String.format(Endpoints.GET_METADATA, assetType, id, language);
        return webClientHelper.sendGetRequest(path, ShutterstockAudioMetadataDto.class).getBody();
    }

    public ContributorDTO getContributorMetadata(String id) {
        String path = Endpoints.GET_CONTRIBUTOR + id;
        log.info(path);
        return webClientHelper.sendGetRequest(path, ContributorDTO.class).getBody();
    }

    public JsonNode getLicencedImagesRaw(Integer page, Integer perPage, String assetType) {
        String path = String.format(Endpoints.GET_IMAGES, assetType, page, perPage);
        return webClientHelper.sendGetRequest(path, JsonNode.class, Object.class).getBody();
    }

    public String requestImageDownloadUrl(String id, String assetType) {
        String emptyJson = "{}";
        JsonNode node = webClientHelper.sendPostRequest(String.format(Endpoints.LICENSED_IMAGE, assetType, id), emptyJson, JsonNode.class).getBody();
        return node != null ? node.path("url").asText(null) : null;
    }
}
