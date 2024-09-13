package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetItemFieldsTO {

    private AssetFieldTO id;
    private AssetFieldTO title;

    public AssetItemFieldsTO() {
    }

    public AssetItemFieldsTO(final AssetFieldTO id,final AssetFieldTO title) {
        this.id = id;
        this.title = title;
    }

    public AssetFieldTO getId() {
        return id;
    }

    public void setId(final AssetFieldTO id) {
        this.id = id;
    }

    public AssetFieldTO getTitle() {
        return title;
    }

    public void setTitle(final AssetFieldTO title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetItemFieldsTO)) return false;
        AssetItemFieldsTO that = (AssetItemFieldsTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "AssetItemFieldsTO{" +
                "id=" + id +
                ", title=" + title +
                '}';
    }
}
