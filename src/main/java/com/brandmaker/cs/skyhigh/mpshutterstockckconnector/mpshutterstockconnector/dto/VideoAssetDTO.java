package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoAssetDTO {
    @JsonProperty("thumb_webm")
    private VideoUrlDTO thumbWebm;
    @JsonProperty("thumb_mp4")
    private VideoUrlDTO thumbMp4;
    @JsonProperty("preview_webm")
    private VideoUrlDTO previewWebm;
    @JsonProperty("preview_mp4")
    private VideoUrlDTO previewMp4;
    @JsonProperty("thumb_jpg")
    private VideoUrlDTO thumbJpg;
    @JsonProperty("preview_jpg")
    private VideoUrlDTO previewJpg;
    @JsonProperty("thumb_jpgs")
    private VideoUrlsDTO thumbJpgs;
    private VideoWebFormatDTO web;
    private VideoSdFormatDTO sd;
    private VideoHdFormatDTO hd;
    @JsonProperty("4k")
    private Video4kFormatDTO fourK;
}
