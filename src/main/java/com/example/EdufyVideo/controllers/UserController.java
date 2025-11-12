package com.example.EdufyVideo.controllers;

import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.services.PlaylistService;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

    //ED-78-AA
    @RestController
    @RequestMapping("/video")
    @PreAuthorize("hasRole('video_user')")
    public class UserController {

        private final VideoService videoService;
        private final PlaylistService playlistService;

        @Autowired
        public UserController(VideoService videoService, PlaylistService playlistService) {
            this.videoService = videoService;
            this.playlistService = playlistService;
        }

    //ED-57-AA
    @GetMapping("/videoclip-title")
    public ResponseEntity<List<VideoClipResponseDTO>> getVideoClipByTitle (@RequestParam String title) {
        return ResponseEntity.ok(videoService.getVideoClipsByTitle(title));
    }

    //ED-59-AA
    @GetMapping("/playlist-title")
    public ResponseEntity<List<VideoPlaylistResponseDTO>> getVideoPlaylistByTitle (@RequestParam String title) {
            return ResponseEntity.ok(playlistService.getPlaylistsByTitle(title));
    }




}