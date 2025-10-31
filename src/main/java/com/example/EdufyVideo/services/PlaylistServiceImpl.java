package com.example.EdufyVideo.services;

import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoPlaylistResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Ed-79-AA
@Service
public class PlaylistServiceImpl implements PlaylistService {

    //Ed-79-AA
    private final PlaylistRepository playlistRepository;

    //ED-79-AA
    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }


    //Ed-79-AA
    @Override
    public VideoPlaylistResponseDTO getVideoPlaylistById(Long id) {
        VideoPlaylist playlist = playlistRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("VideoPlaylist", "id", id));

        //TODO implement API-response from Creator
        return VideoPlaylistResponseMapper.toDto(playlist);
    }
}
