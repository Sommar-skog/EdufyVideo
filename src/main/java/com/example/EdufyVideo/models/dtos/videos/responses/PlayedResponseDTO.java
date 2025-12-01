package com.example.EdufyVideo.models.dtos.videos.responses;

public class PlayedResponseDTO {

    private String url;

    public PlayedResponseDTO() {}

    public PlayedResponseDTO(String url) {
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
        return "PlayedResponseDTO{" +
                "url='" + url + '\'' +
                '}';
    }
}
