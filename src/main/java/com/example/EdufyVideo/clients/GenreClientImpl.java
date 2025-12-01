package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.clients.genres.MediaByGenreDTO;
import com.example.EdufyVideo.models.dtos.clients.genres.RegisterMediaGenreDTO;
import com.example.EdufyVideo.models.dtos.clients.genres.GenreDTO;
import com.example.EdufyVideo.models.enums.MediaType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Objects;

//ED-268-AA
@Service
public class GenreClientImpl implements GenreClient {

    private final RestClient restClient;
    private final Keycloak keycloak;

    public GenreClientImpl(RestClient.Builder builder, Keycloak keycloak) {
        this.restClient = builder.baseUrl("http://gateway:4545/api/v1/genre").build();
        this.keycloak = keycloak;
    }

    //ED-243-AA
    public GenreDTO getGenreById(Long genreId) {
        try {
            return restClient.get()
                    .uri("/{id}", genreId)
                    .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .body(GenreDTO.class);
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyGenre");
        }
    }

    public List<GenreDTO> getGenresByMediaTypeAndMediaId (MediaType mediaType, Long mediaId) {
        try {
            return restClient.get()
                    .uri("/by/media-id/{mediaType}/{mediaId}", mediaType, mediaId)
                    .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<GenreDTO>>() {
                    });
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyGenre");
        }
    }

    //ED-270-AA
    public MediaByGenreDTO getVideoClipsByGenre (Long genreId, MediaType mediaType){
            try {
                return restClient.get()
                        .uri("/{genreId}/media/by-type/{mediaType}", genreId, mediaType)
                        .header("Authorization", "Bearer " + keycloak.getAccessToken())
                        .retrieve()
                        .body(MediaByGenreDTO.class);
            } catch (ResourceAccessException e) {
                throw new RestClientException("EdufyVideo", "EdufyGenre");
            } catch (RestClientResponseException ex) {
            // Client Call returns 400/404/409/500
            String error = ex.getResponseBodyAsString();
            throw new InvalidInputException("Genre-service error: " + error);
        }
    }

    //ED-244-AA
    public boolean createRecordeOfMedia(MediaType mediaType, Long mediaId, List<Long> genreIds) {
        try {
            ResponseEntity<Void> response = restClient.post()
                    .uri("/media/record")
                    .body(new RegisterMediaGenreDTO( mediaId,mediaType, genreIds))
                    .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .toBodilessEntity();

            return response.getStatusCode() == HttpStatus.CREATED;

        } catch (RestClientResponseException ex) {
            // Client Call returns 400/404/409/500
            String error = ex.getResponseBodyAsString();
            throw new InvalidInputException("Genre-service error: " + error);

        } catch (ResourceAccessException ex) {
            //Can not reach Thumb at all
            throw new RestClientException("EdufyVideo", "EdufyGenre");
        }
    }

    //ED-278-AA – Helper to return genres names directly
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
            return List.of("GENRE UNKNOWN " + e.getMessage());
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
            return List.of("GENRE UNKNOWN " + e.getMessage());
        }
    }

}
