package com.example.EdufyVideo.models.dtos;

public class MediaDTO {

    private Long mediaId;

    public MediaDTO() {}

    public MediaDTO(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String toString() {
        return "MediaDTO{" +
                "id=" + mediaId +
                '}';
    }
}
