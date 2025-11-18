package com.example.EdufyVideo.models.dtos;

import java.util.List;

//ED-270-AA
public class MedaByGenreDTO {

    private String genreName;
    private List<Long> mediaIds;

    public MedaByGenreDTO() {

    }

    public MedaByGenreDTO(String genreName, List<Long> mediaIds) {
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
        return "MedaByGenreDTO{" +
                "genreName='" + genreName + '\'' +
                ", mediaIds=" + mediaIds +
                '}';
    }
}
