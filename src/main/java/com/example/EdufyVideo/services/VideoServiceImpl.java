package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.*;
import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.exceptions.UniqueConflictException;
import com.example.EdufyVideo.models.dtos.clients.users.UserDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.dtos.videos.inputs.AddVideoClipDTO;
import com.example.EdufyVideo.models.dtos.videos.inputs.PlayedDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.VideoClipResponseDTO;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.models.enums.MediaType;
import com.example.EdufyVideo.repositories.VideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//ED-78-AA
@Service
public class VideoServiceImpl implements VideoService {

    //ED-78-AA
    private final VideoRepository videoRepository;
    private final CreatorClient creatorClient;
    private final GenreClient genreClient;
    private final UserClient userClient;
    private final ThumbClient thumbClient; //ED-243-AA
    private final PlaylistService playlistService;


    //ED-78-AA
    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CreatorClient creatorClient, GenreClient genreClient, UserClient userClient, PlaylistService playlistService, ThumbClient thumbClient) {
        this.videoRepository = videoRepository;
        this.creatorClient = creatorClient;
        this.genreClient = genreClient;
        this.userClient = userClient;
        this.playlistService = playlistService;
        this.thumbClient = thumbClient;
    }

    //ED-78-AA
    @Override
    public VideoClipResponseDTO getVideoClipById(Long id, Collection<? extends GrantedAuthority> roles) {
        VideoClip video;

        //ED-252-AA filter roles
        if(roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            video = videoRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoClip", "id", id));
            return VideoClipResponseMapper.toDTOAdmin(video, creatorClient, genreClient);
        } else{
            video = videoRepository.findVideoClipByIdAndActiveTrue(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoClip", "id", id));
            return VideoClipResponseMapper.toDTOUser(video, creatorClient, genreClient);
        }
    }

    //ED-57-AA
    @Override
    public List<VideoClipResponseDTO> getVideoClipsByTitle(String title) {
        List<VideoClip> videoClips = videoRepository.findVideoClipByTitleContainingIgnoreCaseAndActiveTrue(title);

        if(videoClips.isEmpty()){
            throw new ResourceNotFoundException("VideoClip", "title containing", title);
        }

        return videoClips.stream()
                .map(v -> VideoClipResponseMapper.toDTOUser(v, creatorClient, genreClient))
                .collect(Collectors.toList());
    }

    //ED-84-AA
    @Override
    public List<VideoClipResponseDTO> getAllVideoClips(Authentication authentication) {
        List<VideoClip> videoClips;

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            videoClips = videoRepository.findAll();
            return videoClips.stream()
                    .map(v -> VideoClipResponseMapper.toDTOAdmin(v, creatorClient, genreClient))
                    .collect(Collectors.toList());
        } else{
            videoClips = videoRepository.findAllByActiveTrue();
            return videoClips.stream()
                    .map(v -> VideoClipResponseMapper.toDTOUser(v, creatorClient, genreClient))
                    .collect(Collectors.toList());
        }
    }

    //ED-255-AA
    @Override
    public PlayedDTO playVideoClip(Long videoClipId, Authentication authentication) {
        UserDTO user = userClient.getUserBySub(authentication.getName());

        if (user.getId() == null){
            throw new ResourceNotFoundException("UserClientImpl returned id null");
        }

        VideoClip clip = videoRepository.findVideoClipByIdAndActiveTrue(videoClipId).orElseThrow(
                () -> new ResourceNotFoundException("Active VideoClip", "id", videoClipId)
        );

        if (!clip.getUserHistory().containsKey(user.getId())){
            clip.getUserHistory().put(user.getId(), 0L);
        }

        Long current = clip.getUserHistory().get(user.getId());
        Long updated = current + 1;
        clip.getUserHistory().put(user.getId(), updated);
        videoRepository.save(clip);

        return new PlayedDTO(clip.getUrl());
    }

    //ED-282-AA
    @Override
    public List<VideoClipResponseDTO> getUserHistory(Long userId) {
        List<Long> videoClipsUserHistory = videoRepository.findVideoIdsByUserIdInHistory(userId);

        if (videoClipsUserHistory.isEmpty()){
            return Collections.emptyList();
        }

        return videoClipsUserHistory.stream().map(VideoClipResponseMapper::toDTOClientJustId).collect(Collectors.toList());
    }

    //ED-243-AA
    @Override
    @Transactional//ED-244-AA
    public VideoClipResponseDTO addVideoClip(AddVideoClipDTO addVideoClipDTO) {
        validateVideoClipData(addVideoClipDTO);

        VideoClip videoClip = new VideoClip(
                addVideoClipDTO.getTitle(),
                addVideoClipDTO.getUrl(),
                addVideoClipDTO.getDescription(),
                addVideoClipDTO.getLength());

       VideoClip savedClip = videoRepository.save(videoClip);

        if (addVideoClipDTO.getPlaylistIds() != null && !addVideoClipDTO.getPlaylistIds().isEmpty()) {
            playlistService.addVideoClipToPlaylists(addVideoClipDTO.getPlaylistIds(), savedClip);
        }

        genreClient.createRecordeOfMedia(MediaType.VIDEO_CLIP, savedClip.getId(), addVideoClipDTO.getGenreIds());
        thumbClient.createRecordeOfMedia(MediaType.VIDEO_CLIP, savedClip.getId(), savedClip.getTitle());
        creatorClient.createRecordeOfMedia(MediaType.VIDEO_CLIP, savedClip.getId(), addVideoClipDTO.getCreatorIds());


        VideoClip reloaded = videoRepository.findWithPlaylists(savedClip.getId());

        return VideoClipResponseMapper.toDTOAdmin(reloaded, creatorClient, genreClient);
    }

    //ED-243-AA
    private void validateVideoClipData(AddVideoClipDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new InvalidInputException("Title cannot be null or blank");
        }
        if (dto.getTitle().length() > 100) {
            throw new InvalidInputException("Title cannot exceed 100 characters");
        }
        if (dto.getUrl() == null || dto.getUrl().isBlank()) {
            throw new InvalidInputException("Url cannot be null or blank");
        }
        String trimmedUrl = dto.getUrl().trim();
        if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            throw new InvalidInputException("Url must start with http:// or https://");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new InvalidInputException("Description cannot be null or blank");
        }
        if (dto.getDescription().length() > 255) {
            throw new InvalidInputException("Description cannot exceed 255 characters");
        }
        if (dto.getLength() == null) {
            throw new InvalidInputException("Length cannot be null");
        }
        if (dto.getLength().equals(LocalTime.MIDNIGHT)) {
            throw new InvalidInputException("Length cannot be 00:00:00");
        }
        if (dto.getGenreIds() == null || dto.getGenreIds().isEmpty()) {
            throw new InvalidInputException("At least one genres must be provided");
        }

        if (dto.getCreatorIds() == null || dto.getCreatorIds().isEmpty()) {
            throw new InvalidInputException("At least one creator must be provided");
        }

        if (dto.getPlaylistIds() != null && !dto.getPlaylistIds().isEmpty()) {
            for (Long id : dto.getPlaylistIds()) {
                if (id <= 0){
                    throw new InvalidInputException("Playlist id must be a positive number");
                }
            }
        }
        validateUniqueUrl(trimmedUrl);
    }

    //ED-243-AA
    private void validateUniqueUrl(String url) {
        if (videoRepository.existsByUrl(url)) {
            throw new UniqueConflictException("url", url);
        }
    }
}
