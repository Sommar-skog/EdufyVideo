package com.example.EdufyVideo.controllers;

import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.services.PlaylistService;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//ED-252/AA
@RestController
@RequestMapping("/video/")
@PreAuthorize("hasAnyRole('video_user', 'video_admin')")
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

    //ED-84-AA
    @GetMapping("/videoclips-all")
    public ResponseEntity<List<VideoClipResponseDTO>> getAllVideoClips(Authentication authentication) {
        return ResponseEntity.ok(videoService.getAllVideoClips(authentication));
    }

    //ED-85-AA
    @GetMapping("/videoplaylists-all")
    public ResponseEntity<List<VideoPlaylistResponseDTO>> getAllVideoPlaylists(Authentication authentication) {
        return ResponseEntity.ok(playlistService.getAllPlaylists(authentication));
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

    //ED-61-AA
    @GetMapping("/videography-creator/{creatorId}")
    public ResponseEntity<List<VideographyResponseDTO>> getVideographyByCreator (@PathVariable Long creatorId, Authentication auth) {
        return null;
    }
}
