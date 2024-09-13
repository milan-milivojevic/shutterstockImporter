package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchPagingTO {

    @JsonProperty("@type")
    private String type;
    private int offset;
    private int limit;

    public SearchPagingTO() {
    }

    public SearchPagingTO(final String type, final int offset, final int limit) {
        this.type = type;
        this.offset = offset;
        this.limit = limit;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchPagingTO)) return false;
        SearchPagingTO that = (SearchPagingTO) o;
        return offset == that.offset &&
                limit == that.limit &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, offset, limit);
    }

    @Override
    public String toString() {
        return "SearchPagingTO{" +
                "type='" + type + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
