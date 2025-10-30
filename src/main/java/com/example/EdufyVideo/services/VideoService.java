package com.example.EdufyVideo.services;


import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;

public interface VideoService {

    //ED-78-AA
    VideoClipResponseDTO getVideoClipById(Long id);
}
