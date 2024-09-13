package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchCriteriaTO {

    @JsonProperty("@type")
    private String type;
    private List<SearchSubCriteriaTO> subs;

    public SearchCriteriaTO() {
    }

    public SearchCriteriaTO(final String type, final List<SearchSubCriteriaTO> subs) {
        this.type = type;
        this.subs = subs;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<SearchSubCriteriaTO> getSubs() {
        return subs;
    }

    public void setSubs(final List<SearchSubCriteriaTO> subs) {
        this.subs = subs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchCriteriaTO)) return false;
        SearchCriteriaTO that = (SearchCriteriaTO) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(subs, that.subs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, subs);
    }

    @Override
    public String toString() {
        return "SearchCriteriaTO{" +
                "type='" + type + '\'' +
                ", subs=" + subs +
                '}';
    }
}
