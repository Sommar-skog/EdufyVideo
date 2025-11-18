package com.example.EdufyVideo.models.dtos;

import java.util.List;

//ED-270-AA
public class MediaByGenreDTO {

    private String genreName;
    private List<Long> mediaIds;

    public MediaByGenreDTO() {

    }

    public MediaByGenreDTO(String genreName, List<Long> mediaIds) {
        this.genreName = genreName;
        this.mediaIds = mediaIds;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public List<Long> getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(List<Long> mediaIds) {
        this.mediaIds = mediaIds;
    }

    @Override
    public String toString() {
        return "MediaByGenreDTO{" +
                "genreName='" + genreName + '\'' +
                ", mediaIds=" + mediaIds +
                '}';
    }
}
