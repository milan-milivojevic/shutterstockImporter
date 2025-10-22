package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetSearchTO {

    @JsonValue
    private final ObjectNode root;

    private AssetSearchTO(ObjectNode root) {
        this.root = root;
    }

    public static AssetSearchTO fromObjectNode(ObjectNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Asset search payload cannot be null");
        }
        return new AssetSearchTO(node);
    }
}