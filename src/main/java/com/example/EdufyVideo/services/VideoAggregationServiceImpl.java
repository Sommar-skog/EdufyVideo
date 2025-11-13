package com.example.EdufyVideo.services;

import com.example.EdufyVideo.models.dtos.VideographyResponseDTO;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

//ED-61-AA
@Service
public class VideoAggregationServiceImpl implements VideoAggregationService {

    private final VideoRepository videoRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public VideoAggregationServiceImpl(VideoRepository videoRepository, PlaylistRepository playlistRepository) {
        this.videoRepository = videoRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public VideographyResponseDTO getVideographyByCreator(Long creatorId, Authentication authentication) {
        return null;
    }
}
