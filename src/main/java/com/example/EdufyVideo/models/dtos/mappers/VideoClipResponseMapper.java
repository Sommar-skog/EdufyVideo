package com.example.EdufyVideo.models.dtos.mappers;

import com.example.EdufyVideo.clients.CreatorClient;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.PlaylistInfoDTO;
import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.enteties.PlaylistEntry;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.models.enums.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoClipResponseMapper {


    //ED-61-AA
    public static VideoClipResponseDTO toDTOAdmin(VideoClip videoClip, CreatorClient creatorClient) {
        List<String> creators = creatorClient
                .getCreatorsByMediaTypeAndMediaId(MediaType.VIDEO_CLIP, videoClip.getId())
                .stream()
                .map(c -> c.getId() + " - " + c.getUsername())
                .collect(Collectors.toList());

        List<PlaylistInfoDTO> playlists = getPlaylistInfoDTOS(videoClip);

        VideoClipResponseDTO dto = new VideoClipResponseDTO();
        dto.setId(videoClip.getId());
        dto.setTitle(videoClip.getTitle());
        dto.setCreatorUsernames(creators);
        dto.setDescription(videoClip.getDescription());
        // dto.setGenreNames(genreNames); //TODO getGenres later
        dto.setUrl(videoClip.getUrl());
        dto.setLength(videoClip.getLength());
        dto.setReleaseDate(videoClip.getReleaseDate());
        dto.setTimesPlayed(videoClip.getTimesPlayed());
        dto.setPlaylists(playlists);
        dto.setActive(videoClip.isActive());

        return dto;
    }

    public static VideoClipResponseDTO toDTOUser(VideoClip videoClip, CreatorClient creatorClient) {
        List<String> usernames = creatorClient
                .getCreatorsByMediaTypeAndMediaId(MediaType.VIDEO_CLIP, videoClip.getId())
                .stream()
                .map(CreatorDTO::getUsername)
                .collect(Collectors.toList());
        List<PlaylistInfoDTO> playlists = getPlaylistInfoDTOS(videoClip);

        VideoClipResponseDTO dto = new VideoClipResponseDTO();
        dto.setTitle(videoClip.getTitle());
        dto.setCreatorUsernames(usernames);
        dto.setDescription(videoClip.getDescription());
        // dto.setGenreNames(genreNames); //TODO getGenres later
        dto.setUrl(videoClip.getUrl());
        dto.setLength(videoClip.getLength());
        dto.setReleaseDate(videoClip.getReleaseDate());
        dto.setTimesPlayed(videoClip.getTimesPlayed());
        dto.setPlaylists(playlists);

        return dto;
    }

/*
    //ED-78-AA
    //TODO lägg till inparametrar för API-anrop
    public static VideoClipResponseDTO toDto(VideoClip videoClip) {
        List<PlaylistInfoDTO> playlists = getPlaylistInfoDTOS(videoClip);
        List<String> genreNames = getGenreNames(videoClip);
        List<String> creatorUsernames = getCreators(videoClip);

        VideoClipResponseDTO dto = new VideoClipResponseDTO();
        dto.setId(videoClip.getId());
        dto.setTitle(videoClip.getTitle());
        dto.setCreatorUsernames(creatorUsernames);
        dto.setDescription(videoClip.getDescription());
        dto.setGenreNames(genreNames);
        dto.setUrl(videoClip.getUrl());
        dto.setLength(videoClip.getLength());
        dto.setReleaseDate(videoClip.getReleaseDate());
        dto.setTimesPlayed(videoClip.getTimesPlayed());
        dto.setPlaylists(playlists);
        dto.setActive(videoClip.isActive());

        return dto;
    }

    //ED-61-AA
    public static VideoClipResponseDTO toDtoWithDataFromService(VideoClip videoClip */
/*List<String> genreNames*//*
, List<String> creatorUsernames) {
        List<PlaylistInfoDTO> playlists = getPlaylistInfoDTOS(videoClip);

        VideoClipResponseDTO dto = new VideoClipResponseDTO();
        dto.setId(videoClip.getId());
        dto.setTitle(videoClip.getTitle());
        dto.setCreatorUsernames(creatorUsernames);
        dto.setDescription(videoClip.getDescription());
        // dto.setGenreNames(genreNames); //TODO getGenres later
        dto.setUrl(videoClip.getUrl());
        dto.setLength(videoClip.getLength());
        dto.setReleaseDate(videoClip.getReleaseDate());
        dto.setTimesPlayed(videoClip.getTimesPlayed());
        dto.setPlaylists(playlists);
        dto.setActive(videoClip.isActive());

        return dto;
    }
*/

/*    private static List<String> getCreators(VideoClip videoClip) {
        List<String> creatorUsernames = new ArrayList<>();

        if (videoClip.getCreatorIds() != null) {
            for (Long creatorId : videoClip.getCreatorIds()) {
                //TODO API-anrop eller inskickat från service
                String creatorUsername = "Creator id: " + creatorId; //TODO implement creator
                creatorUsernames.add(creatorUsername != null ? creatorUsername : "CREATOR UNKNOWN");
            }
        }
        return creatorUsernames;
    }*/

    private static List<String> getGenreNames(VideoClip videoClip) {
        List<String> genreNames = new ArrayList<>();

        if (videoClip.getGenresIds() != null) {
            for (Long genreId : videoClip.getGenresIds()) {
                //TODO API-anrop eller inskickat från service
                String genreName = "Genre id: " + genreId;  //TODO implement genre
                genreNames.add(genreName != null ? genreName : "UNKNOWN");
            }
        }
        return genreNames;
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
