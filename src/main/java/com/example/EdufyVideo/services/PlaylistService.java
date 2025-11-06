package com.example.EdufyVideo.services;

import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface PlaylistService {

    //ED-79-AA
    VideoPlaylistResponseDTO getVideoPlaylistById (Long id, Collection<? extends GrantedAuthority> roles); //ED-252-AA (roles)

    //ED-59-AA
    List<VideoPlaylistResponseDTO> getPlaylistsByTitle (String title);
}
