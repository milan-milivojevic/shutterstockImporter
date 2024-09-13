package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContributorDataDTO {
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    private String about;
    private List<String> equipment;
    @JsonProperty("contributor_type")
    private List<String> contributorType;
    private List<String> styles;
    private List<String> subjects;
    private String website;
    private String location;
    @JsonProperty("portfolio_url")
    private String portfolioUrl;
    @JsonProperty("social_media")
    private Map<String, String> socialMedia;
}
