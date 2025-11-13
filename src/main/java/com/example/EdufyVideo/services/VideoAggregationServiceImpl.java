package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.VideographyResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.repositories.PlaylistRepository;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
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

    @Override
    public VideographyResponseDTO getVideographyByCreator(Long creatorId, Authentication authentication) {
        CreatorDTO creatorDTO = creatorClient.getCreatorWithMediaLists(creatorId);

        List<Long> clips = creatorDTO.getVideoClips();
        List<Long> playlist = creatorDTO.getVideoPlaylists();

        List<VideoClip> videos;
        List<VideoPlaylist> playlists;

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_user"))){
            videos = getActiveMediaList(creatorDTO.getVideoClips(), videoRepository, VideoClip::isActive);
            playlists = getActiveMediaList(creatorDTO.getVideoPlaylists(), playlistRepository, VideoPlaylist::isActive);
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            videos = getAllMediaList(creatorDTO.getVideoClips(), videoRepository);
            playlists = getAllMediaList(creatorDTO.getVideoPlaylists(), playlistRepository);
        }



        return new VideographyResponseDTO(
                videos.stream().map(VideoClipResponseMapper::toDtoWithDataFromService())
        );
    }

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

    private <T> List<T> getAllMediaList(List<Long> ids, JpaRepository<T, Long> repo) {
        return ids.stream()
                .map(repo::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
