package com.example.EdufyVideo.services;

import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    //ED-78-AA
    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    //ED-78-AA
    @Override
    public VideoClipResponseDTO getVideoClipById(Long id, Collection<? extends GrantedAuthority> roles) {
        VideoClip video;

        //ED-252-AA filter roles
        if(roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_video_admin"))){
            video = videoRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoClip", "id", id));
        } else{
            video = videoRepository.findVideoClipByIdAndActiveTrue(id).orElseThrow(() ->
                    new ResourceNotFoundException("VideoClip", "id", id));
        }


        //TODO implement API-response from Genre and Creator
        return VideoClipResponseMapper.toDto(video);
    }

    //ED-57-AA

    @Override
    public List<VideoClipResponseDTO> getVideoClipByTitle(String title) {
        List<VideoClip> videoClips = videoRepository.findVideoClipByTitleContainingIgnoreCaseAndActiveTrue(title);

        if(videoClips.isEmpty()){
            throw new ResourceNotFoundException("VideoClip", "title containing", title);
        }
        return videoClips.stream().map(VideoClipResponseMapper::toDto).collect(Collectors.toList());
    }
}
