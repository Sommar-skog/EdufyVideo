package com.example.EdufyVideo.services;

import com.example.EdufyVideo.models.dtos.videos.responses.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.VideographyResponseDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

//ED-61-AA
public interface VideoAggregationService {

    //ED-61-AA
    VideographyResponseDTO getVideographyByCreator (Long creatorId, Authentication authentication);

    //ED-270-AA
    List<VideoClipResponseDTO> getVideoClipsByGenre(Long genreId);
}
