package com.example.EdufyVideo.services;


import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface VideoService {

    //ED-78-AA
    VideoClipResponseDTO getVideoClipById(Long id, Collection<? extends GrantedAuthority> roles); //ED-252-AA roles

    //ED-57-AA
    List<VideoClipResponseDTO> getVideoClipsByTitle(String title);

    //ED-84-AA
    List<VideoClipResponseDTO> getAllVideoClips(Authentication authentication);

    List<VideoClipResponseDTO> getUserHistory(Authentication authentication);
}
