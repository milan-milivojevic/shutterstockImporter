package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchSubCriteriaTO {

    @JsonProperty("@type")
    private String type;
    private List<String> fields;
    private String value;

    public SearchSubCriteriaTO() {
    }

    public SearchSubCriteriaTO(final String type, final List<String> fields, final String value) {
        this.type = type;
        this.fields = fields;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(final List<String> fields) {
        this.fields = fields;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchSubCriteriaTO)) return false;
        SearchSubCriteriaTO that = (SearchSubCriteriaTO) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, fields, value);
    }

    @Override
    public String toString() {
        return "SearchSubCriteriaTO{" +
                "type='" + type + '\'' +
                ", fields=" + fields +
                ", value='" + value + '\'' +
                '}';
    }
}
