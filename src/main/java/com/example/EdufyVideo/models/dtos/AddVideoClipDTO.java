package com.example.EdufyVideo.models.dtos;

import java.time.LocalTime;
import java.util.List;

public class AddVideoClipDTO {

    private String title;
    private String description;
    private List<Long> creatorIds;
    private String url;
    private LocalTime length;
    private List<Long> genreIds;
    private Long playlistId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getCreatorIds() {
        return creatorIds;
    }

    public void setCreatorIds(List<Long> creatorIds) {
        this.creatorIds = creatorIds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalTime getLength() {
        return length;
    }

    public void setLength(LocalTime length) {
        this.length = length;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public String toString() {
        return "AddVideoClipDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creatorIds=" + creatorIds +
                ", url='" + url + '\'' +
                ", length=" + length +
                ", genreIds=" + genreIds +
                ", playlistId=" + playlistId +
                '}';
    }
}
