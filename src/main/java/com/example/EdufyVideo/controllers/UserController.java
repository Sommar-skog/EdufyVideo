package com.example.EdufyVideo.controllers;


import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    //ED-78-AA
    @RestController
    @RequestMapping("/video")
    public class UserController {

        //ED-78-AA
        private final VideoService videoService;

        //ED-78-AA
        @Autowired
        public UserController(VideoService videoService) {
            this.videoService = videoService;
        }

        //ED-78-AA
        @GetMapping("/videoclip/{id}")
        public ResponseEntity<VideoClipResponseDTO> getVideoClipById (@PathVariable Long id) {
            return ResponseEntity.ok(videoService.getVideoClipById(id));
        }


}