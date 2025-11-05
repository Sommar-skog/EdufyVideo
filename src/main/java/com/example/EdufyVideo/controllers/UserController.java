package com.example.EdufyVideo.controllers;

import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//ED-78-AA
    @RestController
    @RequestMapping("/video")
    public class UserController {

        private final VideoService videoService;

        @Autowired
        public UserController(VideoService videoService) {
            this.videoService = videoService;
        }

    //ED-57-AA
    @GetMapping("/videoclip-title")
    public ResponseEntity<List<VideoClipResponseDTO>> getVideoClipByTitle (@RequestParam String title) {
        return ResponseEntity.ok(videoService.getVideoClipByTitle(title));
    }




}