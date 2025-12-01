package com.example.EdufyVideo.models.dtos.videos.inputs;

import java.util.List;

//ED-244-AA
public class AddPlaylistDTO {

    private String title;
    private String url;
    private String description;
    private List<Long> creatorIds;

    public AddPlaylistDTO() {}

    public AddPlaylistDTO(String title, String url, String description, List<Long> creatorIds) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.creatorIds = creatorIds;
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

    public List<Long> getCreatorIds() {
        return creatorIds;
    }

    public void setCreatorIds(List<Long> creatorIds) {
        this.creatorIds = creatorIds;
    }

    @Override
    public String toString() {
        return "AddPlaylistDTO{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", creatorIds=" + creatorIds +
                '}';
    }
}
