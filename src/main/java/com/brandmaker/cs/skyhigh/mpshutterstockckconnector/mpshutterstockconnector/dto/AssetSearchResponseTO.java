package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetSearchResponseTO {

    @JsonProperty("@type")
    private String type;
    private List<AssetSearchItemTO> items;
    private SearchPagingTO paging;
    private int totalHits;

    public AssetSearchResponseTO() {
    }

    public AssetSearchResponseTO(final String type, final List<AssetSearchItemTO> items, final SearchPagingTO paging,
                                 final int totalHits) {
        this.type = type;
        this.items = items;
        this.paging = paging;
        this.totalHits = totalHits;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<AssetSearchItemTO> getItems() {
        return items;
    }

    public void setItems(final List<AssetSearchItemTO> items) {
        this.items = items;
    }

    public SearchPagingTO getPaging() {
        return paging;
    }

    public void setPaging(final SearchPagingTO paging) {
        this.paging = paging;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(final int totalHits) {
        this.totalHits = totalHits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetSearchResponseTO)) return false;
        AssetSearchResponseTO that = (AssetSearchResponseTO) o;
        return totalHits == that.totalHits &&
                Objects.equals(type, that.type) &&
                Objects.equals(items, that.items) &&
                Objects.equals(paging, that.paging);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, items, paging, totalHits);
    }

    @Override
    public String toString() {
        return "AssetSearchResponseTO{" +
                "type='" + type + '\'' +
                ", items=" + items +
                ", paging=" + paging +
                ", totalHits=" + totalHits +
                '}';
    }
}
