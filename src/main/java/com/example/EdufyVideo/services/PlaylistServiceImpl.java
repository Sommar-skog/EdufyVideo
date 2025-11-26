package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.exceptions.UniqueConflictException;
import com.example.EdufyVideo.models.dtos.*;
import com.example.EdufyVideo.models.dtos.mappers.VideoPlaylistResponseMapper;
import com.example.EdufyVideo.models.enteties.PlaylistEntry;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.models.enums.MediaType;
import com.example.EdufyVideo.repositories.PlaylistEntryRepository;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import com.example.EdufyVideo.repositories.VideoRepository;
import jakarta.transaction.Transactional;
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
    private final PlaylistEntryRepository playlistEntryRepository; //ED-315-AA
    private final VideoRepository videoRepository;
    private final CreatorClient creatorClient;


    //ED-79-AA
    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository,PlaylistEntryRepository playlistEntryRepository, VideoRepository videoRepository, CreatorClient creatorClient) {
        this.playlistRepository = playlistRepository;
        this.playlistEntryRepository = playlistEntryRepository;
        this.videoRepository = videoRepository;
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

    //ED-315-AA
    @Override
    public VideoPlaylistResponseDTO addVideoClipsToPlaylist(Long playlistId, AddVClipToPlaylistDTO addVClipToPlaylistDTO) {
        VideoPlaylist playlist = playlistRepository.findById(playlistId).orElseThrow(
                () -> new ResourceNotFoundException("VideoPlaylist", "id", playlistId)
        );

        List<Long> clipIds = addVClipToPlaylistDTO.getVideoClipIds();
        for (Long clipId : clipIds) {
            VideoClip videoClip = videoRepository.findById(clipId).orElseThrow(
                    () -> new ResourceNotFoundException("VideoClip", "id", clipId)
            );
            addVideoClipToPlaylists(List.of(playlistId),videoClip);
        }
        playlistRepository.save(playlist);


        return VideoPlaylistResponseMapper.toSimpleDtoAdmin(playlist, creatorClient);
    }

    //ED-315-AA (changed from videoservice to playlist service)
    @Override
    public void addVideoClipToPlaylists(List<Long> playlistIds, VideoClip videoClip) {

        for (Long playlistId : playlistIds) {
            VideoPlaylist playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", playlistId));

            int position = playlistEntryRepository.countByPlaylistId(playlistId) + 1;

            PlaylistEntry entry = new PlaylistEntry();
            entry.setPlaylist(playlist);
            entry.setVideoClip(videoClip);
            entry.setPosition(position);
            playlistEntryRepository.save(entry);
            videoClip.getPlaylistEntries().add(entry);
        }
    }

    //ED-244-AA
    private void validatePlaylistData(AddPlaylistDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new InvalidInputException("Playlist title cannot be null or blank");
        }
        if (dto.getTitle().length() > 100) {
            throw new InvalidInputException("Title cannot exceed 100 characters");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new InvalidInputException("Playlist description cannot be null or blank");
        }
        if (dto.getDescription().length() > 255) {
            throw new InvalidInputException("Description cannot exceed 255 characters");
        }
        if (dto.getUrl() == null || dto.getUrl().isBlank()) {
            throw new InvalidInputException("Playlist url cannot be null or blank");
        }
        String trimmedUrl = dto.getUrl().trim();
        if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            throw new InvalidInputException("Url must start with http:// or https://");
        }

        validateUniqueUrl(trimmedUrl);
    }


    //ED-244-AA
    private void validateUniqueUrl(String url) {
        if (playlistRepository.existsByUrl(url)) {
            throw new UniqueConflictException("url", url);
        }
    }
}
