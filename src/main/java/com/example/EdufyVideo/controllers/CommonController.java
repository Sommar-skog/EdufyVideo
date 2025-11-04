package com.example.EdufyVideo.controllers;

import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.services.PlaylistService;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//ED-252/AA
@RestController
@RequestMapping("/video/")
@PreAuthorize("hasAnyAuthority('video_user', 'video_admin')")
public class CommonController {

    //ED-252-AA
    private final VideoService videoService;
    private final PlaylistService playlistService;

    //ED-252-AA
    @Autowired
    public CommonController(VideoService videoService, PlaylistService playlistService) {
        this.videoService = videoService;
        this.playlistService = playlistService;
    }

    //ED-78-AA
    @GetMapping("/videoclip/{id}")
    public ResponseEntity<VideoClipResponseDTO> getVideoClipById (@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(videoService.getVideoClipById(id, auth.getAuthorities()));
    }

    //ED-79-AA
    @GetMapping("/videoplaylist/{id}")
    public ResponseEntity<VideoPlaylistResponseDTO> getVideoPlaylistById (@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(playlistService.getVideoPlaylistById(id, auth.getAuthorities()));
    }
}
