package com.example.EdufyVideo.models.dtos.videos.responses.mappers;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.clients.GenreClient;
import com.example.EdufyVideo.models.dtos.videos.responses.PlaylistInfoDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.VideoClipResponseDTO;
import com.example.EdufyVideo.models.entities.PlaylistEntry;
import com.example.EdufyVideo.models.entities.VideoClip;
import com.example.EdufyVideo.models.enums.MediaType;

import java.util.ArrayList;
import java.util.List;

public class VideoClipResponseMapper {


    //ED-61-AA //ED-277-AA
    public static VideoClipResponseDTO toDTOAdmin(VideoClip videoClip, CreatorClient creatorClient, GenreClient genreClient) {
        List<PlaylistInfoDTO> playlists = getPlaylistInfoDTOS(videoClip);

        VideoClipResponseDTO dto = new VideoClipResponseDTO();
        dto.setId(videoClip.getId());
        dto.setTitle(videoClip.getTitle());
        dto.setCreatorUsernames(
                creatorClient.getCreatorIdAndUsernameByMedia(MediaType.VIDEO_CLIP, videoClip.getId())
        );
        dto.setDescription(videoClip.getDescription());
        dto.setGenreNames(
                genreClient.getGenreIdAndNameByMedia(MediaType.VIDEO_CLIP, videoClip.getId())
        );
        dto.setUrl(videoClip.getUrl());
        dto.setLength(videoClip.getLength());
        dto.setReleaseDate(videoClip.getReleaseDate());
        dto.setTimesPlayed(videoClip.getTimesPlayed());
        dto.setPlaylists(playlists);
        dto.setActive(videoClip.isActive());

        return dto;
    }

    //ED-277-AA
    public static VideoClipResponseDTO toDTOUser(VideoClip videoClip, CreatorClient creatorClient, GenreClient genreClient) {
        List<PlaylistInfoDTO> playlists = getPlaylistInfoDTOS(videoClip);

        VideoClipResponseDTO dto = new VideoClipResponseDTO();
        dto.setTitle(videoClip.getTitle());
        dto.setCreatorUsernames(
                creatorClient.getCreatorUsernamesByMedia(MediaType.VIDEO_CLIP, videoClip.getId())
        );
        dto.setDescription(videoClip.getDescription());
        dto.setGenreNames(
                genreClient.getGenreNamesByMedia(MediaType.VIDEO_CLIP, videoClip.getId())
        );
        dto.setUrl(videoClip.getUrl());
        dto.setLength(videoClip.getLength());
        dto.setReleaseDate(videoClip.getReleaseDate());
        dto.setTimesPlayed(videoClip.getTimesPlayed());
        dto.setPlaylists(playlists);

        return dto;
    }

    //ED-282-AA
    public static VideoClipResponseDTO toDTOClientJustId(Long videoClipId) {
        VideoClipResponseDTO dto = new VideoClipResponseDTO();
        dto.setId(videoClipId);
        return dto;
    }

    private static List<PlaylistInfoDTO> getPlaylistInfoDTOS(VideoClip videoClip) {
        List<PlaylistInfoDTO> playlists = new ArrayList<>();

        if(videoClip.getPlaylistEntries() != null) {
            for (PlaylistEntry playlistEntry : videoClip.getPlaylistEntries()) {
                if (playlistEntry.getPlaylist() != null) {
                    PlaylistInfoDTO playlistInfoDTO = new PlaylistInfoDTO();
                    playlistInfoDTO.setPlaylistName(playlistEntry.getPlaylist().getTitle());
                    playlistInfoDTO.setPosition(playlistEntry.getPosition());
                    playlists.add(playlistInfoDTO);
                }
            }
        }
        return playlists;
    }


}
