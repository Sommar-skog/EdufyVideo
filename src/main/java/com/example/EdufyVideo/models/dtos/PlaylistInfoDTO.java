package com.example.EdufyVideo.models.dtos;

public class PlaylistInfoDTO {

    private String playlistName;
    private Integer position;

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PlaylistInfoDTO{" +
                "playlistName='" + playlistName + '\'' +
                ", position=" + position +
                '}';
    }
}
