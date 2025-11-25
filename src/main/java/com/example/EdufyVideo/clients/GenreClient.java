package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.models.dtos.GenreDTO;
import com.example.EdufyVideo.models.dtos.MediaByGenreDTO;
import com.example.EdufyVideo.models.enums.MediaType;

import java.util.List;

//ED-345-AA
public interface GenreClient {
    GenreDTO getGenreById(Long genreId);
    List<GenreDTO> getGenresByMediaTypeAndMediaId (MediaType mediaType, Long mediaId);
    MediaByGenreDTO getVideoClipsByGenre (Long genreId, MediaType mediaType);
    boolean createRecordeOfMedia(MediaType mediaType, Long mediaId, List<Long> genreIds);
    List<String> getGenreNamesByMedia(MediaType mediaType, long mediaId);
    List<String> getGenreIdAndNameByMedia(MediaType mediaType, long mediaId);
}
