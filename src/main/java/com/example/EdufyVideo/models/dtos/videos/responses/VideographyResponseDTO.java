package com.example.EdufyVideo.models.dtos.videos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

//ED-61-AA
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideographyResponseDTO {

    List<VideoClipResponseDTO> clips;
    List<VideoPlaylistResponseDTO> playlists;

    public VideographyResponseDTO() {

    }

    public VideographyResponseDTO(List<VideoClipResponseDTO> clips,List<VideoPlaylistResponseDTO> playlists) {
        this.clips = clips;
        this.playlists = playlists;
    }

    public List<VideoClipResponseDTO> getClips() {
        return clips;
    }

    public void setClips(List<VideoClipResponseDTO> clips) {
        this.clips = clips;
    }

    public List<VideoPlaylistResponseDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<VideoPlaylistResponseDTO> playlists) {
        this.playlists = playlists;
    }
}
