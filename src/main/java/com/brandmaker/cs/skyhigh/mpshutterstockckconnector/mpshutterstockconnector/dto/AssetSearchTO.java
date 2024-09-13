package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetSearchTO {

    private String searchSchemaId;
    private String lang;
    private SearchOutputTO output;
    private SearchCriteriaTO criteria;

    public AssetSearchTO() {
    }

    public AssetSearchTO(final String searchSchemaId, final String lang, final SearchOutputTO output,
                         final SearchCriteriaTO criteria) {
        this.searchSchemaId = searchSchemaId;
        this.lang = lang;
        this.output = output;
        this.criteria = criteria;
    }

    public String getSearchSchemaId() {
        return searchSchemaId;
    }

    public void setSearchSchemaId(final String searchSchemaId) {
        this.searchSchemaId = searchSchemaId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(final String lang) {
        this.lang = lang;
    }

    public SearchOutputTO getOutput() {
        return output;
    }

    public void setOutput(final SearchOutputTO output) {
        this.output = output;
    }

    public SearchCriteriaTO getCriteria() {
        return criteria;
    }

    public void setCriteria(final SearchCriteriaTO criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetSearchTO)) return false;
        AssetSearchTO that = (AssetSearchTO) o;
        return Objects.equals(searchSchemaId, that.searchSchemaId) &&
                Objects.equals(lang, that.lang) &&
                Objects.equals(output, that.output) &&
                Objects.equals(criteria, that.criteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchSchemaId, lang, output, criteria);
    }

    @Override
    public String toString() {
        return "AssetSearchTO{" +
                "searchSchemaId='" + searchSchemaId + '\'' +
                ", lang='" + lang + '\'' +
                ", output=" + output +
                ", criteria=" + criteria +
                '}';
    }
}
