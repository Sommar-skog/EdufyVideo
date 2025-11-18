package com.example.EdufyVideo.models.dtos;

import com.example.EdufyVideo.models.enums.MediaType;

import java.util.ArrayList;
import java.util.List;

//ED-243-AA
public class RegisterMediaCreatorDTO {
    private Long mediaId;
    private MediaType mediaType;
    private List<Long> creatorIds = new ArrayList<>();

    public RegisterMediaCreatorDTO() {}

    public RegisterMediaCreatorDTO(Long mediaId, MediaType mediaType, List<Long> creatorIds) {
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.creatorIds = creatorIds;
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

    public List<Long> getCreatorIds() {
        return creatorIds;
    }

    public void setCreatorIds(List<Long> creatorIds) {
        this.creatorIds = creatorIds;
    }

    @Override
    public String toString() {
        return "RegisterMediaCreatorDTO{" +
                "mediaId=" + mediaId +
                ", mediaType=" + mediaType +
                ", creatorIds=" + creatorIds +
                '}';
    }
}
