package com.example.EdufyVideo.models.dtos.videos.responses;

import com.example.EdufyVideo.models.enteties.VideoClip;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//ED-78-AA
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoClipResponseDTO {

    private Long id;
    private String title;
    private List<String> creatorUsernames;
    private String description;
    private List<String> genreNames;
    private String url;
    private LocalTime length;
    private LocalDate releaseDate;
    private Long timesPlayed;
    private List<PlaylistInfoDTO> playlists;
    private Boolean active;

    //ED-79-AA
    public VideoClipResponseDTO() {}

    //ED-79-AA
    public VideoClipResponseDTO(Long id, String title, List<String> creatorUsernames, String description, List<String> genreNames, String url, LocalTime length, LocalDate releaseDate, List<PlaylistInfoDTO> playlists, boolean active) {
        this.id = id;
        this.title = title;
        this.creatorUsernames = creatorUsernames;
        this.description = description;
        this.genreNames = genreNames;
        this.url = url;
        this.length = length;
        this.releaseDate = releaseDate;
        this.playlists = playlists;
        this.active = active;
    }

    //ED-79-AA
    public VideoClipResponseDTO(VideoClip videoClip, List<PlaylistInfoDTO> playlists, List<String> creatorUsernames, List<String> genreNames) {
        this.id = videoClip.getId();
        this.title = videoClip.getTitle();
        this.creatorUsernames = creatorUsernames;
        this.genreNames = genreNames;
        this.description = videoClip.getDescription();
        this.url = videoClip.getUrl();
        this.length = videoClip.getLength();
        this.releaseDate = videoClip.getReleaseDate();
        this.playlists = playlists;
        this.active = videoClip.isActive();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getLength() {
        return length;
    }

    public void setLength(LocalTime length) {
        this.length = length;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(Long timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public List<String> getCreatorUsernames() {
        return creatorUsernames;
    }

    public void setCreatorUsernames(List<String> creatorUsernames) {
        this.creatorUsernames = creatorUsernames;
    }

    public List<String> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(List<String> genreNames) {
        this.genreNames = genreNames;
    }

    public List<PlaylistInfoDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistInfoDTO> playlists) {
        this.playlists = playlists;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "VideoClipResponseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                ", timesPlayed=" + timesPlayed +
                ", creatorUsernames=" + creatorUsernames +
                ", genreNames=" + genreNames +
                ", playlists=" + playlists +
                ", active=" + active +
                '}';
    }
}
