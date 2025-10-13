package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simplified response object for Media Pool asset search,
 * providing both raw items list and extracted assetId as a String.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetSearchResponseTO {

    private final List<JsonNode> items;
    private final String assetId;

    /**
     * Parses the response JSON, populates items array and extracts assetId
     * from: items[0].fields.versions.items[0].fields.assetId.value
     */
    @JsonCreator
    public AssetSearchResponseTO(JsonNode root) {
        JsonNode itemsNode = root.path("items");
        if (itemsNode.isArray() && itemsNode.size() > 0) {
            List<JsonNode> temp = new ArrayList<>();
            for (JsonNode node : itemsNode) {
                temp.add(node);
            }
            this.items = temp;
            // extract assetId as text
            JsonNode versionItems = itemsNode.get(0)
              .path("fields")
              .path("versions")
              .path("items");
            if (versionItems.isArray() && versionItems.size() > 0) {
                JsonNode idNode = versionItems.get(0)
                  .path("fields")
                  .path("assetId")
                  .path("value");
                this.assetId = idNode.isNull() ? null : idNode.asText();
            } else {
                this.assetId = null;
            }
        } else {
            this.items = Collections.emptyList();
            this.assetId = null;
        }
    }

    /**
     * Returns the raw items JSON list from the response.
     */
    public List<JsonNode> getItems() {
        return items;
    }

    /**
     * Returns the extracted assetId as String, or null if not found.
     */
    public String getAssetId() {
        return assetId;
    }
}