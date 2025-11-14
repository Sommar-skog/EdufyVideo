package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.clients.GenreClient;
import com.example.EdufyVideo.clients.UserClient;
import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.exceptions.UniqueConflictException;
import com.example.EdufyVideo.models.dtos.*;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //ED-78-AA
    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CreatorClient creatorClient, GenreClient genreClient, UserClient userClient) {
        this.videoRepository = videoRepository;
        this.creatorClient = creatorClient;
        this.genreClient = genreClient;
        this.userClient = userClient;
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
    //TODO Add thumb for VideoClip, add videoclip to Cretors id-list, add videoClip to genres id-list
    //TODO Save to repository, return to Postman
    @Override
    public VideoClipResponseDTO addVideoClip(AddVideoClipDTO addVideoClipDTO) {
        List<CreatorDTO> creators = validateCreators(addVideoClipDTO.getCreatorIds());
        List<GenreDTO> genres = validateGenres(addVideoClipDTO.getGenreIds());
        validateVideoClipData(addVideoClipDTO);




        return null;
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
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new InvalidInputException("Description cannot be null or blank");
        }
        if (dto.getLength() == null) {
            throw new InvalidInputException("Length cannot be null or blank");
        }
        if (dto.getGenreIds() == null || dto.getGenreIds().isEmpty()) {
            throw new InvalidInputException("At least one genre must be provided");
        }
        if (dto.getCreatorIds() == null || dto.getCreatorIds().isEmpty()) {
            throw new InvalidInputException("At least one creator must be provided");
        }

        validateUniqueUrl(dto.getUrl());
    }

    //ED-243-AA
    private void validateUniqueUrl(String url) {
        if (videoRepository.existsByUrl(url)) {
            throw new UniqueConflictException("url", url);
        }
    }

    //ED-243-AA
    private List<CreatorDTO> validateCreators(List<Long> creatorIds) {
        List<CreatorDTO> creators = new ArrayList<>();
        creatorIds.forEach(id -> {
            CreatorDTO creator = creatorClient.getCreatorById(id);
            if (creator == null) {
                throw new ResourceNotFoundException("Creator", "id", id);
            }
            creators.add(creator);
        });
        return creators;
    }

    //ED-243-AA
    private List<GenreDTO> validateGenres(List<Long> genreIds) {
        List<GenreDTO> genres = new ArrayList<>();
        genreIds.forEach(id -> {
            GenreDTO genre = genreClient.getGenreById(id);
            if (genre == null) {
                throw new ResourceNotFoundException("Genre", "id", id);
            }
            genres.add(genre);
        });
        return genres;
    }
}
