package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetSearchItemTO {

    @JsonProperty("@type")
    private String type;
    private AssetItemFieldsTO fields;

    public AssetSearchItemTO() {
    }

    public AssetSearchItemTO(final String type, final AssetItemFieldsTO fields) {
        this.type = type;
        this.fields = fields;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public AssetItemFieldsTO getFields() {
        return fields;
    }

    public void setFields(final AssetItemFieldsTO fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetSearchItemTO)) return false;
        AssetSearchItemTO that = (AssetSearchItemTO) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, fields);
    }

    @Override
    public String toString() {
        return "AssetSearchItem{" +
                "type='" + type + '\'' +
                ", fields=" + fields +
                '}';
    }
}
