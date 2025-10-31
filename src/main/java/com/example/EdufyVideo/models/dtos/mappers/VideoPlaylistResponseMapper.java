package com.example.EdufyVideo.models.dtos.mappers;

import com.example.EdufyVideo.models.dtos.VideoClipInfoDTO;
import com.example.EdufyVideo.models.dtos.VideoPlaylistResponseDTO;
import com.example.EdufyVideo.models.enteties.PlaylistEntry;
import com.example.EdufyVideo.models.enteties.VideoPlaylist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//ED-79-AA
public class VideoPlaylistResponseMapper {

    public static VideoPlaylistResponseDTO toDto(VideoPlaylist videoPlaylist){
        List<String> creatorUsernames = getCreators(videoPlaylist);
        List<VideoClipInfoDTO> videoClipEntries = getVideoClipEntries(videoPlaylist);

        VideoPlaylistResponseDTO dto = new VideoPlaylistResponseDTO();
        dto.setId(videoPlaylist.getId());
        dto.setTitle(videoPlaylist.getTitle());
        dto.setCreatorUsernames(creatorUsernames);
        dto.setDescription(videoPlaylist.getDescription());
        dto.setUrl(videoPlaylist.getUrl());
        dto.setCreationDate(videoPlaylist.getCreationDate());
        dto.setVideoClipEntries(videoClipEntries);
        dto.setActive(videoPlaylist.isActive());
        dto.setActive(videoPlaylist.isActive());

        return dto;
    }

    private static List<String> getCreators(VideoPlaylist videoPlaylist){

        List<String> creatorUsernames = new ArrayList<>();

        if (videoPlaylist.getCreatorIds() != null) {
            for ( Long id : videoPlaylist.getCreatorIds()) {
                String creatorUsername = null; //TODO API-anrop eller inskickat från service
                creatorUsernames.add(creatorUsername != null ? creatorUsername : "CreatorId: " + id);
            }
        }
        return creatorUsernames;
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
