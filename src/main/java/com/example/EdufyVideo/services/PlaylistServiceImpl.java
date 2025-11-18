package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.clients.GenreClient;
import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.exceptions.UniqueConflictException;
import com.example.EdufyVideo.models.dtos.*;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.dtos.mappers.VideoPlaylistResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.models.enums.MediaType;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
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

    //ED-244-AA
    @Override
    @Transactional
    public VideoPlaylistResponseDTO addPlaylist(AddPlaylistDTO addPlaylistDTO) {
        List<CreatorDTO> creators = validateCreators(addPlaylistDTO.getCreatorIds());

        validatePlaylistData(addPlaylistDTO);

        VideoPlaylist playlist = new VideoPlaylist(
                addPlaylistDTO.getTitle(),
                addPlaylistDTO.getUrl(),
                addPlaylistDTO.getDescription()
        );

        VideoPlaylist savedPlaylist = playlistRepository.save(playlist);

        creatorClient.createRecordeOfMedia(MediaType.VIDEO_PLAYLIST, savedPlaylist.getId(), addPlaylistDTO.getCreatorIds());
        return VideoPlaylistResponseMapper.toSimpleDtoAdmin(savedPlaylist, creatorClient);
    }

    //ED-244-AA
    private void validatePlaylistData(AddPlaylistDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new InvalidInputException("Playlist title cannot be null or blank");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new InvalidInputException("Playlist description cannot be null or blank");
        }
        if (dto.getUrl() == null || dto.getUrl().isBlank()) {
            throw new InvalidInputException("Playlist url cannot be null or blank");
        }

        validateUniqueUrl(dto.getUrl());
    }


    //ED-244-AA
    private void validateUniqueUrl(String url) {
        if (playlistRepository.existsByUrl(url)) {
            throw new UniqueConflictException("url", url);
        }
    }

    //ED-244-AA
    private List<CreatorDTO> validateCreators(List<Long> creatorIds) {
        List<CreatorDTO> creators = new ArrayList<>();

        creatorIds.forEach(id -> {
            try{
                CreatorDTO creator = creatorClient.getCreatorById(id);
                creators.add(creator);
            } catch (RestClientResponseException e) {
                throw new ResourceNotFoundException("Creator", "id", id);
            }
        });
        return creators;
    }
}
