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
public class Video4kFormatDTO {
    private int height;
    private int width;
    private int fps;
    private String format;
    @JsonProperty("file_size")
    private long fileSize;
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("is_licensable")
    private boolean isLicensable;
}
