package com.example.EdufyVideo.controllers;

import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//ED-282
@RestController
@RequestMapping("/video")
@PreAuthorize("hasRole('microservice_access')")
public class ClientController {

    private VideoService videoService;

    @Autowired
    public ClientController(VideoService videoService) {
        this.videoService = videoService;
    }

    //ED-282-AA
    @GetMapping("/user-history/{userId}")
    public ResponseEntity<List<VideoClipResponseDTO>> getUserHistory(@PathVariable Long userId){
        return ResponseEntity.ok(videoService.getUserHistory(userId));
    }
}
