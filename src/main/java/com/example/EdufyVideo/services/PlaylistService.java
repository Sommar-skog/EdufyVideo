package com.example.EdufyVideo.services;

import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;

public interface PlaylistService {

    //ED-79-AA
    VideoPlaylistResponseDTO getVideoPlaylistById (Long id);
}
