package com.example.EdufyVideo.controllers;

import com.example.EdufyVideo.models.dtos.videos.inputs.PlayedDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.services.PlaylistService;
import com.example.EdufyVideo.services.VideoAggregationService;
import com.example.EdufyVideo.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//ED-78-AA
@RestController
@RequestMapping("/video")
@PreAuthorize("hasRole('video_user')")
public class UserController {

    private final VideoService videoService;
    private final PlaylistService playlistService;
    private final VideoAggregationService videoAggregationService;

    @Autowired
    public UserController(VideoService videoService, PlaylistService playlistService, VideoAggregationService videoAggregationService) {
        this.videoService = videoService;
        this.playlistService = playlistService;
        this.videoAggregationService = videoAggregationService;
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

    //ED-270-AA
    @GetMapping("/videoclips/{genreId}")
    public ResponseEntity<List<VideoClipResponseDTO>> getVideoClipsByGenre (@PathVariable Long genreId) {
            return ResponseEntity.ok(videoAggregationService.getVideoClipsByGenre(genreId));
    }

    //ED-255-AA
    @GetMapping("/play/{videoClipId}")
    public ResponseEntity<PlayedDTO> playVideoClip (@PathVariable Long videoClipId, Authentication auth){
            return ResponseEntity.ok(videoService.playVideoClip(videoClipId, auth));
    }
}