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
public class ImageJpgDTO {
    @JsonProperty("display_name")
    private String displayName;
    private int dpi;
    @JsonProperty("file_size")
    private long fileSize;
    private String format;
    private int height;
    @JsonProperty("is_licensable")
    private boolean isLicensable;
    private int width;
}
