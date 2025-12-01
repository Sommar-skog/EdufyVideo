package com.example.EdufyVideo.models.dtos.videos.responses;

import com.example.EdufyVideo.models.enteties.PlaylistEntry;

public class VideoClipInfoDTO {

    private String videoClipTitle;
    private Integer videoClipPositionInPlaylist;

    public VideoClipInfoDTO() {}

    public VideoClipInfoDTO(String videoClipTitle, Integer videoClipPositionInPlaylist) {
        this.videoClipTitle = videoClipTitle;
        this.videoClipPositionInPlaylist = videoClipPositionInPlaylist;
    }

    public VideoClipInfoDTO(PlaylistEntry playlistEntry) {
        this.videoClipTitle = playlistEntry.getVideoClip().getTitle();
        this.videoClipPositionInPlaylist = playlistEntry.getPosition();
    }

    public String getVideoClipTitle() {
        return videoClipTitle;
    }

    public void setVideoClipTitle(String videoClipTitle) {
        this.videoClipTitle = videoClipTitle;
    }

    public Integer getVideoClipPositionInPlaylist() {
        return videoClipPositionInPlaylist;
    }

    public void setVideoClipPositionInPlaylist(Integer videoClipPositionInPlaylist) {
        this.videoClipPositionInPlaylist = videoClipPositionInPlaylist;
    }

    @Override
    public String toString() {
        return "VideoClipInfoDTO{" +
                "videoClipTitle='" + videoClipTitle + '\'' +
                ", videoClipPositionInPlaylist=" + videoClipPositionInPlaylist +
                '}';
    }
}
