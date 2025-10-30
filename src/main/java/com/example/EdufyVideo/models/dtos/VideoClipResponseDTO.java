package com.example.EdufyVideo.models.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
                '}';
    }
}
