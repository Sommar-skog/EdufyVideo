package com.example.EdufyVideo.models.dtos;

public class MediaDTO {

    private Long id;

    public MediaDTO() {}

    public MediaDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MediaDTO{" +
                "id=" + id +
                '}';
    }
}
