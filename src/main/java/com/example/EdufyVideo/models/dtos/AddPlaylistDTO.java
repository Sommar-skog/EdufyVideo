package com.example.EdufyVideo.models.dtos;

import java.time.LocalDate;

//ED-244-AA
public class AddPlaylistDTO {

    private String title;
    private String url;
    private String description;

    public AddPlaylistDTO() {}

    public AddPlaylistDTO(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
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

    @Override
    public String toString() {
        return "AddPlaylistDTO{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
