package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants.ApiEndpointConstants;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.AssetMetadataTO;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.helpers.WebClientHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class MediaPoolService {

    private final WebClientHelper webClientHelper;
    private final MediaPoolSearchPayloadFactory searchPayloadFactory;

    public MediaPoolService(WebClientHelper webClientHelper, MediaPoolSearchPayloadFactory searchPayloadFactory) {
        this.webClientHelper = webClientHelper;
        this.searchPayloadFactory = searchPayloadFactory;
    }

    public void updateAssetMetadata(final AssetMetadataTO assetMetadataTO) {
        webClientHelper.sendPatchRequest(
          ApiEndpointConstants.REST_MP_ASSETS,
          assetMetadataTO,
          MediaType.APPLICATION_JSON,
          Object.class
        ).getBody();
    }

    public JsonNode searchByStockTitleValue(final String stockValue) {
        ObjectNode payload = searchPayloadFactory.buildVectorUpdatePayload(stockValue);
        return webClientHelper
          .sendPostJson(ApiEndpointConstants.REST_MP_SEARCH, payload)
          .getBody();
    }

    public JsonNode getAssetVersions(final long assetId) {
        return webClientHelper
          .sendGetJson(ApiEndpointConstants.REST_MP_V10_ASSET_VERSIONS, assetId)
          .getBody();
    }

    public void setOfficialVersion(final long assetId, final long version) {
        webClientHelper
          .sendPostJson(ApiEndpointConstants.REST_MP_V10_SET_OFFICIAL, java.util.Collections.emptyMap(), assetId, version)
          .getBody();
    }

    public void removeVersion(final long assetId, final long version) {
        final String url = ApiEndpointConstants.REMOVE_MEDIA_VERSION
          + "?ajax=true&mediaGUID=" + assetId
          + "&versionNumber=" + version;
        webClientHelper
          .sendPostEmptyBody(url)
          .getBody();
    }

    public void uploadAssetVersion(final long assetId,
                                   final String comment,
                                   final Resource fileResource,
                                   final String filename,
                                   final MediaType contentType) {
        final MultiValueMap<String, ?> parts = webClientHelper.buildMultipart(
          "comment",
          comment,
          "uploadFile",
          fileResource,
          contentType,
          filename
        );
        webClientHelper
          .sendPostMultipart(ApiEndpointConstants.REST_MP_V10_ASSET_VERSIONS, parts, assetId)
          .getBody();
    }

    public static boolean isVectorOfficial(final JsonNode item) {
        final String ext = item.at("/fields/currentVersion/fileResource/extension/value").asText("");
        return "eps".equalsIgnoreCase(ext);
    }

    public static long readAssetId(final JsonNode item) {
        return item.at("/fields/id/value").asLong();
    }
}
