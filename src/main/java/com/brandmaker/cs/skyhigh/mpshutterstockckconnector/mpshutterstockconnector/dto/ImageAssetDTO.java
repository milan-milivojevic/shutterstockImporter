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
public class ImageAssetDTO {
    @JsonProperty("vector_eps")
    private ImageVectorEpsDTO vectorEps;
    @JsonProperty("medium_jpg")
    private ImageJpgDTO mediumJpg;
    @JsonProperty("small_jpg")
    private ImageJpgDTO smallJpg;
    @JsonProperty("huge_jpg")
    private ImageJpgDTO hugeJpg;
    private ImagePreviewDTO preview;
    @JsonProperty("small_thumb")
    private ImagePreviewDTO smallThumb;
    @JsonProperty("large_thumb")
    private ImagePreviewDTO largeThumb;
    @JsonProperty("huge_thumb")
    private ImagePreviewDTO hugeThumb;
    private ImagePreviewDTO mosaic;
    @JsonProperty("preview_1000")
    private ImagePreviewDTO preview1000;
    @JsonProperty("preview_1500")
    private ImagePreviewDTO preview1500;
}
