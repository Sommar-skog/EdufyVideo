package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClientImpl;
import com.example.EdufyVideo.clients.GenreClientImpl;
import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.models.dtos.*;
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
import java.util.function.Predicate;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//ED-61-AA
@Service
public class VideoAggregationServiceImpl implements VideoAggregationService {

    private final VideoRepository videoRepository;
    private final PlaylistRepository playlistRepository;
    private final CreatorClientImpl creatorClientImpl;
    private final GenreClientImpl genreClientImpl;

    @Autowired
    public VideoAggregationServiceImpl(VideoRepository videoRepository, PlaylistRepository playlistRepository, CreatorClientImpl creatorClientImpl, GenreClientImpl genreClientImpl) {
        this.videoRepository = videoRepository;
        this.playlistRepository = playlistRepository;
        this.creatorClientImpl = creatorClientImpl;
        this.genreClientImpl = genreClientImpl;
    }

    //ED-270-AA
    @Override
    public List<VideoClipResponseDTO> getVideoClipsByGenre(Long genreId) {
        MediaByGenreDTO mediaByGenreDTO = genreClientImpl.getVideoClipsByGenre(genreId,MediaType.VIDEO_CLIP);

        List<VideoClip> clips = new ArrayList<>();
        mediaByGenreDTO.getMediaIds().forEach(mediaId -> {
            VideoClip videoClip = videoRepository.findById(mediaId).orElseThrow(
                    () -> new InvalidInputException("VideoClip", "videoClipId", mediaId)
            );
            clips.add(videoClip);
        });

        return clips.stream().map(c -> VideoClipResponseMapper.toDTOUser(c, creatorClientImpl, genreClientImpl)).collect(Collectors.toList());
    }

    //ED-61-AA
    @Override
    public VideographyResponseDTO getVideographyByCreator(Long creatorId, Authentication authentication) {
        List<MediaDTO> clipIds = creatorClientImpl.getMediaListFromCreator(creatorId, MediaType.VIDEO_CLIP);
        List<MediaDTO> playlistIds = creatorClientImpl.getMediaListFromCreator(creatorId, MediaType.VIDEO_PLAYLIST);

        List<Long> videoClipIds = clipIds.stream().map(MediaDTO::getId).collect(Collectors.toList());
        List<Long> videoPlaylistIds = playlistIds.stream().map(MediaDTO::getId).collect(Collectors.toList());

        List<VideoClip> videos;
        List<VideoPlaylist> playlists;

        List<VideoPlaylistResponseDTO> playlistDTOs = new ArrayList<>();
        List<VideoClipResponseDTO> clipDTOs = new ArrayList<>();


        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_user"))){
            videos = getActiveMediaList(videoClipIds, videoRepository, VideoClip::isActive);
            playlists = getActiveMediaList(videoPlaylistIds, playlistRepository, VideoPlaylist::isActive);
            clipDTOs = videos.stream()
                    .map(clip -> VideoClipResponseMapper.toDTOUser(clip, creatorClientImpl, genreClientImpl))
                    .toList();

            playlistDTOs = playlists.stream()
                    .map(pl -> VideoPlaylistResponseMapper.toSimpleDtoUser(pl, creatorClientImpl))
                    .toList();
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            videos = getAllMediaList(videoClipIds, videoRepository);
            playlists = getAllMediaList(videoPlaylistIds, playlistRepository);

           clipDTOs = videos.stream()
                    .map(v -> VideoClipResponseMapper.toDTOAdmin(v, creatorClientImpl, genreClientImpl))
                    .toList();

           playlistDTOs = playlists.stream()
                    .map(pl -> VideoPlaylistResponseMapper.toSimpleDtoAdmin(pl, creatorClientImpl))
                    .toList();
        }
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

}
