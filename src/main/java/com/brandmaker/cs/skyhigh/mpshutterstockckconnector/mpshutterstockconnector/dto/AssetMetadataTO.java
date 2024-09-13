package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetMetadataTO {

    private List<String> assetIds = new ArrayList<>();
    private String errorHandlingStrategy;
    private JsonNode updateOperations;

    public AssetMetadataTO() {
    }

    public AssetMetadataTO(final List<String> assetIds, final String errorHandlingStrategy,
                           final JsonNode updateOperations) {
        this.assetIds = assetIds;
        this.errorHandlingStrategy = errorHandlingStrategy;
        this.updateOperations = updateOperations;
    }

    public List<String> getAssetIds() {
        return assetIds;
    }

    public void setAssetIds(final List<String> assetIds) {
        this.assetIds = assetIds;
    }

    public String getErrorHandlingStrategy() {
        return errorHandlingStrategy;
    }

    public void setErrorHandlingStrategy(final String errorHandlingStrategy) {
        this.errorHandlingStrategy = errorHandlingStrategy;
    }

    public JsonNode getUpdateOperations() {
        return updateOperations;
    }

    public void setUpdateOperations(final JsonNode updateOperations) {
        this.updateOperations = updateOperations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetMetadataTO)) return false;
        AssetMetadataTO that = (AssetMetadataTO) o;
        return Objects.equals(assetIds, that.assetIds) &&
                Objects.equals(errorHandlingStrategy, that.errorHandlingStrategy) &&
                Objects.equals(updateOperations, that.updateOperations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetIds, errorHandlingStrategy, updateOperations);
    }

    @Override
    public String toString() {
        return "AssetMetadataTO{" +
                "assetIds=" + assetIds +
                ", errorHandlingStrategy='" + errorHandlingStrategy + '\'' +
                ", updateOperations=" + updateOperations +
                '}';
    }
}
