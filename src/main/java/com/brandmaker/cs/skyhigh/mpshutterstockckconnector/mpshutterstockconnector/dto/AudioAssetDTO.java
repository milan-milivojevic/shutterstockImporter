package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioAssetDTO {
    private AudioFileDTO previewMp3;
    private AudioFileDTO cleanAudio;
    private AudioFileDTO waveform;
    private AudioFileDTO previewOgg;
}
