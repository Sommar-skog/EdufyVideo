package com.example.EdufyVideo.controllers;


import com.example.EdufyVideo.services.PlaylistService;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
@PreAuthorize("hasRole('video_admin')")
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

}
