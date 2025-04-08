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
public class ImageDetailsDTO {
    private String id;
    @JsonProperty("added_date")
    private String addedDate;
    private int aspect;
    private ImageAssetDTO assets;
    private List<CategoryDTO> categories;
    private ContributorIdDTO contributor;
    private String description;
    @JsonProperty("image_type")
    private String imageType;
    @JsonProperty("is_adult")
    private boolean isAdult;
    @JsonProperty("is_editorial")
    private boolean isEditorial;
    @JsonProperty("is_illustration")
    private boolean isIllustration;
    @JsonProperty("has_model_release")
    private boolean hasModelRelease;
    @JsonProperty("has_property_release")
    private boolean hasPropertyRelease;
    private List<String> keywords;
    @JsonProperty("media_type")
    private String mediaType;
    private List<String> modelReleases;
    @JsonProperty("original_filename")
    private String originalFilename;
}
