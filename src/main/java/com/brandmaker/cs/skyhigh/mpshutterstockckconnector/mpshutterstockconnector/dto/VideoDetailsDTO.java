package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoDetailsDTO {
    private String id;
    @JsonProperty("added_date")
    private String addedDate;
    private int aspect;
    @JsonProperty("aspect_ratio")
    private String aspectRatio;
    private Map<String, VideoAssetDTO> assets;
    private List<CategoryDTO> categories;
    private ContributorIdDTO contributor;
    private String description;
    private int duration;
    @JsonProperty("has_model_release")
    private boolean hasModelRelease;
    @JsonProperty("has_property_release")
    private boolean hasPropertyRelease;
    @JsonProperty("is_adult")
    private boolean isAdult;
    @JsonProperty("is_editorial")
    private boolean isEditorial;
    @JsonProperty("is_select")
    private boolean isSelect;
    private List<String> keywords;
    @JsonProperty("media_type")
    private String mediaType;
    @JsonProperty("original_filename")
    private String originalFilename;
    private List<String> modelReleases;
}
