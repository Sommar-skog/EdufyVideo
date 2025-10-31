package com.example.EdufyVideo.controllers;

import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.services.PlaylistService;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
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

    //ED-78-AA
    @GetMapping("/videoclip/{id}")
    public ResponseEntity<VideoClipResponseDTO> getVideoClipById (@PathVariable Long id) {
        return ResponseEntity.ok(videoService.getVideoClipById(id));
    }

    //ED-79-AA
    @GetMapping("/videoplaylist/{id}")
    public ResponseEntity<VideoPlaylistResponseDTO> getVideoPlaylistById (@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getVideoPlaylistById(id));
    }
}
