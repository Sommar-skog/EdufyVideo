package com.example.EdufyVideo.models.dtos.videos.responses;

import com.example.EdufyVideo.models.entities.VideoPlaylist;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

//ED-79-AA
@JsonInclude(JsonInclude.Include.NON_NULL) //ED-264-AA
public class VideoPlaylistResponseDTO {

    private Long id;
    private String title;
    private List<String> creatorUsernames;
    private String description;
    private String url;
    private LocalDate creationDate;
    private List<VideoClipInfoDTO> videoClipEntries;
    private Boolean active;

    public VideoPlaylistResponseDTO() {

    }

    public VideoPlaylistResponseDTO(VideoPlaylist videoPlaylist, List<String> creatorUsernames, List<VideoClipInfoDTO> videoClipEntries) {
        this.id = videoPlaylist.getId();
        this.title = videoPlaylist.getTitle();
        this.description = videoPlaylist.getDescription();
        this.url = videoPlaylist.getUrl();
        this.creationDate = videoPlaylist.getCreationDate();
        this.creatorUsernames = creatorUsernames;
        this.videoClipEntries = videoClipEntries;
        this.active = videoPlaylist.isActive();
    }

    public VideoPlaylistResponseDTO(Long id, String title, List<String> creatorUsernames, String description, String url, LocalDate creationDate, List<VideoClipInfoDTO> videoClipEntries, boolean active) {
        this.id = id;
        this.title = title;
        this.creatorUsernames = creatorUsernames;
        this.description = description;
        this.url = url;
        this.creationDate = creationDate;
        this.videoClipEntries = videoClipEntries;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCreatorUsernames() {
        return creatorUsernames;
    }

    public void setCreatorUsernames(List<String> creatorUsernames) {
        this.creatorUsernames = creatorUsernames;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<VideoClipInfoDTO> getVideoClipEntries() {
        return videoClipEntries;
    }

    public void setVideoClipEntries(List<VideoClipInfoDTO> videoClipEntries) {
        this.videoClipEntries = videoClipEntries;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "VideoPlaylistResponseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", creatorUsernames=" + creatorUsernames +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", creationDate=" + creationDate +
                ", videoClipEntries=" + videoClipEntries +
                ", active=" + active +
                '}';
    }
}
