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
public class AudioDetailsDTO {
    @JsonProperty("vocal_description")
    private String vocalDescription;
    private List<String> keywords;
    private List<ArtistDTO> artists;
    private List<String> genres;
    private List<String> instruments;
    private String id;
    private String isrc;
    private String description;
    @JsonProperty("similar_artists")
    private List<String> similarArtists;
    private List<String> releases;
    @JsonProperty("is_instrumental")
    private boolean isInstrumental;
    private String title;
    @JsonProperty("is_adult")
    private boolean isAdult;
    private String lyrics;
    @JsonProperty("media_type")
    private String mediaType;
    @JsonProperty("recording_version")
    private String recordingVersion;
    private List<String> moods;
    private String language;
    private AudioAssetDTO assets;
    @JsonProperty("shorts_loops_stems")
    private ShortsLoopsStemsDTO shortsLoopsStems;
    private ContributorDTO contributor;
    private int duration;
    @JsonProperty("published_time")
    private String publishedTime;
    @JsonProperty("updated_time")
    private String updatedTime;
    private int bpm;
    @JsonProperty("added_date")
    private String addedDate;
    private String url;
}
