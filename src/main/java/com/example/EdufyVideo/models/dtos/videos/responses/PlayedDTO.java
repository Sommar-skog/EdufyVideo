package com.example.EdufyVideo.models.dtos.videos.responses;

public class PlayedDTO {

    private String url;

    public PlayedDTO() {}

    public PlayedDTO(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PlayedDTO{" +
                "url='" + url + '\'' +
                '}';
    }
}
