package com.example.EdufyVideo.services;

import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//ED-78-AA
@Service
public class VideoServiceImpl implements VideoService {

    //ED-78-AA
    private final VideoRepository videoRepository;

    //ED-78-AA
    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    //ED-78-AA
    @Override
    public VideoClipResponseDTO getVideoClipById(Long id) {
        VideoClip video = videoRepository.findById(id).orElseThrow()
        return null;
    }
}
