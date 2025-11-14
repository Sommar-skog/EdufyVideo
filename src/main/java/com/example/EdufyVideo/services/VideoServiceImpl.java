package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.clients.GenreClient;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//ED-78-AA
@Service
public class VideoServiceImpl implements VideoService {

    //ED-78-AA
    private final VideoRepository videoRepository;
    private final CreatorClient creatorClient;
    private final GenreClient genreClient;

    //ED-78-AA
    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository, CreatorClient creatorClient, GenreClient genreClient) {
        this.videoRepository = videoRepository;
        this.creatorClient = creatorClient;
        this.genreClient = genreClient;
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
}
