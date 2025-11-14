package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.GenreDTO;
import com.example.EdufyVideo.models.enums.MediaType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

//ED-268-AA
@Service
public class GenreClient {

    private final RestClient restClient;

    public GenreClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://EDUFYBENRE").build();
    }

    //ED-243-AA
    public GenreDTO getGenreById(Long genreId) {
        try {
            return restClient.get()
                    .uri("/{id}", genreId)
                    .retrieve()
                    .body(GenreDTO.class);
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyGenre");
        }
    }

    public List<GenreDTO> getGenresByMediaTypeAndMediaId (MediaType mediaType, Long mediaId) {
        try {
            return restClient.get()
                    .uri("/genre/by/media-id/{mediaType}/{mediaId}", mediaType, mediaId) //TODO update url after method is done.
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<GenreDTO>>() {
                    });
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyGenre");
        }
    }

    //ED-278-AA – Helper to return genre names directly
    public List<String> getGenreNamesByMedia(MediaType mediaType, long mediaId) {
        try {
            List<GenreDTO> genres = getGenresByMediaTypeAndMediaId(mediaType, mediaId);

            if (genres == null || genres.isEmpty()) {
                return List.of("GENRE UNKNOWN");
            }

            return genres.stream()
                    .map(GenreDTO::getName)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException e) {
            return List.of("GENRE UNKNOWN");
        }
    }

    //ED-278-AA – Helper to return combined "id - name" strings
    public List<String> getGenreIdAndNameByMedia(MediaType mediaType, long mediaId) {
        try {
            List<GenreDTO> genres = getGenresByMediaTypeAndMediaId(mediaType, mediaId);

            if (genres == null || genres.isEmpty()) {
                return List.of("GENRE UNKNOWN");
            }

            return genres.stream()
                    .map(g -> g.getId() + " - " + g.getName())
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException e) {
            return List.of("GENRE UNKNOWN");
        }
    }

}
