package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchOutputTO {

    private SearchItemsTO items;
    private SearchPagingTO paging;

    public SearchOutputTO() {
    }

    public SearchOutputTO(final SearchItemsTO items, final SearchPagingTO paging) {
        this.items = items;
        this.paging = paging;
    }

    public SearchItemsTO getItems() {
        return items;
    }

    public void setItems(final SearchItemsTO items) {
        this.items = items;
    }

    public SearchPagingTO getPaging() {
        return paging;
    }

    public void setPaging(final SearchPagingTO paging) {
        this.paging = paging;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchOutputTO)) return false;
        SearchOutputTO that = (SearchOutputTO) o;
        return Objects.equals(items, that.items) &&
                Objects.equals(paging, that.paging);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, paging);
    }

    @Override
    public String toString() {
        return "SearchOutputTO{" +
                "items=" + items +
                ", paging=" + paging +
                '}';
    }
}
