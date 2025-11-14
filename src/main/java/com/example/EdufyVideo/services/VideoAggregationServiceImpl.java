package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.models.dtos.VideographyResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.dtos.mappers.VideoPlaylistResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.models.enums.MediaType;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;


import java.util.List;
import java.util.Optional;

//ED-61-AA
@Service
public class VideoAggregationServiceImpl implements VideoAggregationService {

    private final VideoRepository videoRepository;
    private final PlaylistRepository playlistRepository;
    private final CreatorClient creatorClient;

    @Autowired
    public VideoAggregationServiceImpl(VideoRepository videoRepository, PlaylistRepository playlistRepository, CreatorClient creatorClient) {
        this.videoRepository = videoRepository;
        this.playlistRepository = playlistRepository;
        this.creatorClient = creatorClient;
    }

    //ED-61-AA
    @Override
    public VideographyResponseDTO getVideographyByCreator(Long creatorId, Authentication authentication) {
        CreatorDTO creatorWithClips = creatorClient.getCreatorWithMediaList(creatorId, MediaType.VIDEO_CLIP);
        CreatorDTO creatorWithPlaylists = creatorClient.getCreatorWithMediaList(creatorId, MediaType.VIDEO_PLAYLIST);

        //CreatorDTO creatorDTO = creatorClient.getCreatorWithMediaLists(creatorId);

        List<Long> clips = creatorWithClips.getVideoClips();
        List<Long> playlist = creatorWithPlaylists.getVideoPlaylists();

        List<VideoClip> videos =new ArrayList<>();
        List<VideoPlaylist> playlists = new ArrayList<>();

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_user"))){
            videos = getActiveMediaList(clips, videoRepository, VideoClip::isActive);
            playlists = getActiveMediaList(playlist, playlistRepository, VideoPlaylist::isActive);
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            videos = getAllMediaList(clips, videoRepository);
            playlists = getAllMediaList(playlist, playlistRepository);
        }

        List<VideoClipResponseDTO> clipDTOs = videos.stream()
                .map(clip -> {
                    List<String> creators = getCreatorsForMedia(MediaType.VIDEO_CLIP, clip.getId());
                    return VideoClipResponseMapper.toDtoWithDataFromService(clip, creators);
                })
                .toList();

        List<VideoPlaylistResponseDTO> playlistDTOs = playlists.stream()
                .map(pl -> {
                    List<String> creators = getCreatorsForMedia(MediaType.VIDEO_PLAYLIST, pl.getId());
                    return VideoPlaylistResponseMapper.toDtoWithCreatorsFromService(pl, creators);
                })
                .toList();

        return new VideographyResponseDTO(clipDTOs, playlistDTOs);
    }

    //ED-61-AA
    private <T> List<T> getActiveMediaList(List<Long> ids,
                                           JpaRepository<T, Long> repo,
                                           Predicate<T> isActive) {
        return ids.stream()
                .map(repo::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(isActive)
                .toList();
    }

    //ED-61-AA
    private <T> List<T> getAllMediaList(List<Long> ids, JpaRepository<T, Long> repo) {
        return ids.stream()
                .map(repo::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    //ED-61-AA
    private List<String> getCreatorsForMedia(MediaType type, Long mediaId) {
        try {
            List<CreatorDTO> creators = creatorClient.getCreatorsByMediaTypeAndMediaId(type, mediaId);

            if (creators == null || creators.isEmpty()) {
                return List.of("CREATOR UNKNOWN");
            }

            return creators.stream()
                    .map(CreatorDTO::getUsername)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException e) {
            return List.of("CREATOR UNKNOWN");
        }
    }

}
