package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.dtos.mappers.VideoPlaylistResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//Ed-79-AA
@Service
public class PlaylistServiceImpl implements PlaylistService {

    //Ed-79-AA
    private final PlaylistRepository playlistRepository;
    private final CreatorClient creatorClient;

    //ED-79-AA
    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository, CreatorClient creatorClient) {
        this.playlistRepository = playlistRepository;
        this.creatorClient = creatorClient;
    }


    //Ed-79-AA
    @Override
    public VideoPlaylistResponseDTO getVideoPlaylistById(Long id, Collection<? extends GrantedAuthority> roles) {
        VideoPlaylist playlist;

        //ED-252-AA filter roles
        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            playlist = playlistRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoPlaylist", "id", id));
            return VideoPlaylistResponseMapper.toDtoAdmin(playlist, creatorClient);
        } else {
            playlist = playlistRepository.findByIdAndActiveTrue(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoPlaylist", "id", id));
            return VideoPlaylistResponseMapper.toDtoUser(playlist, creatorClient);
        }
    }

    //ED-59-AA
    @Override
    public List<VideoPlaylistResponseDTO> getPlaylistsByTitle(String title) {
        List<VideoPlaylist> playlists = playlistRepository.findVideoPlaylistByTitleContainingIgnoreCaseAndActiveTrue(title);

        if (playlists.isEmpty()) {
            throw new ResourceNotFoundException("VideoPlaylist", "title", title);
        }

        return playlists.stream()
                .map(v -> VideoPlaylistResponseMapper.toDtoUser(v, creatorClient))
                .collect(Collectors.toList());
    }

    //ED-85-AA
    @Override
    public List<VideoPlaylistResponseDTO> getAllPlaylists(Authentication authentication) {
        List<VideoPlaylist> playlists;

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))) {
            playlists = playlistRepository.findAll();
            return playlists.stream()
                    .map(v -> VideoPlaylistResponseMapper.toDtoAdmin(v, creatorClient))
                    .collect(Collectors.toList());
        } else {
            playlists = playlistRepository.findAllByActiveTrue();
            return playlists.stream()
                    .map(v -> VideoPlaylistResponseMapper.toDtoUser(v, creatorClient))
                    .collect(Collectors.toList());
        }
    }
}
