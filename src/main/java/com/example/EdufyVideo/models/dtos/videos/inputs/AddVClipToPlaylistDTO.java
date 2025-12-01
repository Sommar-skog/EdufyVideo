package com.example.EdufyVideo.models.dtos.videos.inputs;

import java.util.ArrayList;
import java.util.List;

//ED-315-AA
public class AddVClipToPlaylistDTO {
    private List<Long> videoClipIds = new ArrayList<>();

    public AddVClipToPlaylistDTO() {}

    public AddVClipToPlaylistDTO(List<Long> videoClipIds) {
        this.videoClipIds = videoClipIds;
    }

    public List<Long> getVideoClipIds() {
        return videoClipIds;
    }

    public void setVideoClipIds(List<Long> videoClipIds) {
        this.videoClipIds = videoClipIds;
    }
}
