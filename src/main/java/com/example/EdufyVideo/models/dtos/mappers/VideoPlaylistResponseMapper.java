package com.example.EdufyVideo.models.dtos.mappers;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.VideoClipInfoDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.models.enteties.PlaylistEntry;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import com.example.EdufyVideo.models.enums.MediaType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//ED-79-AA
public class VideoPlaylistResponseMapper {

    public static VideoPlaylistResponseDTO toDtoAdmin(VideoPlaylist videoPlaylist, CreatorClient creatorClient) {
        List<VideoClipInfoDTO> videoClipEntries = getVideoClipEntries(videoPlaylist);

        VideoPlaylistResponseDTO dto = new VideoPlaylistResponseDTO();
        dto.setId(videoPlaylist.getId());
        dto.setTitle(videoPlaylist.getTitle());
        dto.setCreatorUsernames(creatorClient.getCreatorIdAndUsernameByMedia(MediaType.VIDEO_PLAYLIST, videoPlaylist.getId()));
        dto.setDescription(videoPlaylist.getDescription());
        dto.setUrl(videoPlaylist.getUrl());
        dto.setCreationDate(videoPlaylist.getCreationDate());
        dto.setVideoClipEntries(videoClipEntries);
        dto.setActive(videoPlaylist.isActive());

        return dto;
    }

    public static VideoPlaylistResponseDTO toSimpleDtoAdmin(VideoPlaylist videoPlaylist, CreatorClient creatorClient) {
        VideoPlaylistResponseDTO dto = new VideoPlaylistResponseDTO();
        dto.setId(videoPlaylist.getId());
        dto.setTitle(videoPlaylist.getTitle());
        dto.setCreatorUsernames(creatorClient.getCreatorIdAndUsernameByMedia(MediaType.VIDEO_PLAYLIST, videoPlaylist.getId()));
        dto.setDescription(videoPlaylist.getDescription());
        dto.setUrl(videoPlaylist.getUrl());
        dto.setCreationDate(videoPlaylist.getCreationDate());
        dto.setActive(videoPlaylist.isActive());

        return dto;
    }

    public static VideoPlaylistResponseDTO toDtoUser(VideoPlaylist videoPlaylist, CreatorClient creatorClient) {
        List<VideoClipInfoDTO> videoClipEntries = getVideoClipEntries(videoPlaylist);

        VideoPlaylistResponseDTO dto = new VideoPlaylistResponseDTO();
        dto.setTitle(videoPlaylist.getTitle());
        dto.setCreatorUsernames(creatorClient.getCreatorUsernamesByMedia(MediaType.VIDEO_PLAYLIST, videoPlaylist.getId()));
        dto.setDescription(videoPlaylist.getDescription());
        dto.setUrl(videoPlaylist.getUrl());
        dto.setCreationDate(videoPlaylist.getCreationDate());
        dto.setVideoClipEntries(videoClipEntries);

        return dto;
    }

    public static VideoPlaylistResponseDTO toSimpleDtoUser(VideoPlaylist videoPlaylist, CreatorClient creatorClient) {
        List<String> usernames = creatorClient
                .getCreatorsByMediaTypeAndMediaId(MediaType.VIDEO_PLAYLIST, videoPlaylist.getId())
                .stream()
                .map(CreatorDTO::getUsername)
                .collect(Collectors.toList());

        VideoPlaylistResponseDTO dto = new VideoPlaylistResponseDTO();
        dto.setTitle(videoPlaylist.getTitle());
        dto.setCreatorUsernames(usernames);
        dto.setDescription(videoPlaylist.getDescription());
        dto.setUrl(videoPlaylist.getUrl());
        dto.setCreationDate(videoPlaylist.getCreationDate());

        return dto;
    }

    private static List<VideoClipInfoDTO> getVideoClipEntries(VideoPlaylist videoPlaylist){
        List<VideoClipInfoDTO> videoClipEntries = new ArrayList<>();

        if (videoPlaylist.getEntryList() != null) {
            videoPlaylist.getEntryList().stream()
                    .sorted(Comparator.comparingInt(PlaylistEntry::getPosition))
                    .forEach(entry -> {
                        VideoClipInfoDTO  dto= new VideoClipInfoDTO(entry);
                        videoClipEntries.add(dto);
                    });
        }
        return videoClipEntries;
    }
}
