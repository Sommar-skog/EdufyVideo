package com.example.EdufyVideo.services;

import com.example.EdufyVideo.models.dtos.AddPlaylistDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.models.enteties.VideoClip;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface PlaylistService {

    //ED-79-AA
    VideoPlaylistResponseDTO getVideoPlaylistById (Long id, Collection<? extends GrantedAuthority> roles); //ED-252-AA (roles)

    //ED-59-AA
    List<VideoPlaylistResponseDTO> getPlaylistsByTitle (String title);

    //ED-85-AA
    List<VideoPlaylistResponseDTO> getAllPlaylists(Authentication auth);

    //ED-244-AA
    VideoPlaylistResponseDTO addPlaylist(AddPlaylistDTO addPlaylistDTO);

    //ED-315-AA
    void addVideoClipToPlaylists(List<Long> playlistIds, VideoClip videoClip);

    //ED-315-AA
    VideoPlaylistResponseDTO addVideoClipsToPlaylist(Long playlistId, List<Long> clipIds);
}
