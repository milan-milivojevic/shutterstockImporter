package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants.ApiEndpointConstants;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.AssetMetadataTO;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.helpers.WebClientHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class MediaPoolService {

    private final WebClientHelper webClientHelper;

    public MediaPoolService(WebClientHelper webClientHelper) {
        this.webClientHelper = webClientHelper;
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
        final ObjectMapper om = new ObjectMapper();
        final ObjectNode payload = om.createObjectNode();

        payload.put("searchSchemaId", "asset");
        payload.put("lang", "DE");

        final ObjectNode output = payload.putObject("output");
        final ObjectNode items = output.putObject("items");
        final ArrayNode fields = items.putArray("fields");
        fields.add("id");
        fields.add("title");
        fields.add("currentVersion");
        fields.add("versions");
        fields.add("assetType");
        fields.add("vdb");

        final ObjectNode paging = output.putObject("paging");
        paging.put("@type", "offset");
        paging.put("offset", 0);
        paging.put("limit", 25);

        final ArrayNode sorting = output.putArray("sorting");
        final ObjectNode sort1 = sorting.addObject();
        sort1.put("@type", "field");
        sort1.put("field", "uploadDate");
        sort1.put("asc", false);

        final ObjectNode criteria = payload.putObject("criteria");
        criteria.put("@type", "and");
        final ArrayNode subsRoot = criteria.putArray("subs");

        final ObjectNode orNode = subsRoot.addObject();
        orNode.put("@type", "or");
        final ArrayNode orSubs = orNode.putArray("subs");

        final ObjectNode match = orSubs.addObject();
        match.put("@type", "match");
        final ArrayNode matchFields = match.putArray("fields");
        matchFields.add("description_multi");
        matchFields.add("itemDescription_multi");
        matchFields.add("originalName");
        matchFields.add("assetType.name");
        matchFields.add("title_multi");
        matchFields.add("id");
        match.put("value", stockValue);

        final ObjectNode exactMatch = orSubs.addObject();
        exactMatch.put("@type", "exact_match");
        final ArrayNode exFields = exactMatch.putArray("fields");
        exFields.add("containedText");
        exFields.add("exifIptcXmpData");
        exactMatch.put("value", stockValue);

        final ObjectNode inExt = subsRoot.addObject();
        inExt.put("@type", "in");
        final ArrayNode extFields = inExt.putArray("fields");
        extFields.add("extension");
        final ArrayNode extValues = inExt.putArray("text_value");
        extValues.add("ai");
        extValues.add("bmp");
        extValues.add("eps");
        extValues.add("gif");
        extValues.add("ico");
        extValues.add("jpg/jpeg");
        extValues.add("png");
        extValues.add("psb");
        extValues.add("psd");
        extValues.add("svg");
        extValues.add("tif/tiff");
        extValues.add("wmf");

        final ObjectNode inType = subsRoot.addObject();
        inType.put("@type", "in");
        final ArrayNode f2 = inType.putArray("fields");
        f2.add("assetType.id");
        final ArrayNode v2 = inType.putArray("long_value");
        v2.add("7");
        inType.put("any", true);

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
          .sendPostJson(ApiEndpointConstants.REST_MP_V10_SET_OFFICIAL, new Object(), assetId, version)
          .getBody();
    }

    public void removeVersion(final long assetId, final long version) {
        webClientHelper
          .sendPostJson(ApiEndpointConstants.REMOVE_MEDIA_VERSION, new Object(), assetId, version)
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
        final String ext = item.at("/fields/currentVersion/fileResource/extension/value").asText("").toLowerCase();
        return "eps".equals(ext) || "ai".equals(ext) || "svg".equals(ext) || "wmf".equals(ext);
    }

    public static long readAssetId(final JsonNode item) {
        return item.at("/fields/id/value").asLong();
    }
}
