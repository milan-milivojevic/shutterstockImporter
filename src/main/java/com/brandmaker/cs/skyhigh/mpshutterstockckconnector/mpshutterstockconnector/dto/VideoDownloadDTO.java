package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoDownloadDTO {
    private String id;
    private UserDTO user;
    private String license;
    @JsonProperty("download_time")
    private String downloadTime;
    @JsonProperty("is_downloadable")
    private boolean isDownloadable;
    private VideoDTO video;
    @JsonProperty("subscription_id")
    private String subscriptionId;
    private MetadataDTO metadata;
}
