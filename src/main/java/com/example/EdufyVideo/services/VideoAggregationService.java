package com.example.EdufyVideo.services;

import com.example.EdufyVideo.models.dtos.VideographyResponseDTO;
import org.springframework.security.core.Authentication;

//ED-61-AA
public interface VideoAggregationService {

    //ED-61-AA
    VideographyResponseDTO getVideographyByCreator (Long creatorId, Authentication authentication);
}
