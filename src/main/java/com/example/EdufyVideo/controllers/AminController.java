package com.example.EdufyVideo.controllers;


import com.example.EdufyVideo.models.dtos.AddVideoClipDTO;
import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.services.PlaylistService;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
@PreAuthorize("hasAnyRole('video_admin', 'edufy_realm_admin')")
public class AminController {

    //ED-78-AA
    private final VideoService videoService;
    private final PlaylistService playlistService;

    //ED-78-AA
    @Autowired
    public AminController(VideoService videoService, PlaylistService playlistService) {
        this.videoService = videoService;
        this.playlistService = playlistService;
    }

    //ED-243-AA
    @PostMapping("/videoclip")
    public ResponseEntity<VideoClipResponseDTO> addVideoClip(@RequestBody AddVideoClipDTO dto) {
        return ResponseEntity.ok(videoService.addVideoClip(dto));
    }

    @PostMapping("/playlist")
    public ResponseEntity<VideoPlaylistResponseDTO> addPlaylist(@RequestBody AddPlaylistDTO dto){
        return null;
    }

}
