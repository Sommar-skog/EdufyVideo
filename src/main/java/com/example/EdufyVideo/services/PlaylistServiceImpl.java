package com.example.EdufyVideo.services;

import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoPlaylistResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
    public VideoPlaylistResponseDTO getVideoPlaylistById(Long id, Collection<? extends GrantedAuthority> roles) {
        VideoPlaylist playlist;

        //ED-252-AA filter roles
        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            playlist = playlistRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoPlaylist", "id", id));
        } else {
            playlist = playlistRepository.findByIdAndActiveTrue(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoPlaylist", "id", id));
        }

        //TODO implement API-response from Creator
        return VideoPlaylistResponseMapper.toDto(playlist);
    }
}
