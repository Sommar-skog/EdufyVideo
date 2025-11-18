package com.example.EdufyVideo.models.dtos;

import com.example.EdufyVideo.models.enums.MediaType;

//ED-243-AA
public class RegisterMediaDTO {

    private Long mediaId;

    private MediaType mediaType;

    private String mediaName;

    public RegisterMediaDTO() {}

    public RegisterMediaDTO(Long mediaId, MediaType mediaType, String mediaName) {
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.mediaName = mediaName;
    }

    public Long getMediaId() {
        return mediaId;
    }
    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }
    public MediaType getMediaType() {
        return mediaType;
    }
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
    public String getMediaName() {
        return mediaName;
    }
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    @Override
    public String toString() {
        return "RegisterMediaDTO{" +
                "mediaId=" + mediaId +
                ", mediaType=" + mediaType +
                ", mediaName='" + mediaName + '\'' +
                '}';
    }
}
