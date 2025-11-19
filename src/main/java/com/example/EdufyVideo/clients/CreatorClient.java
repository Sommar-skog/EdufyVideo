package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.RegisterMediaCreatorDTO;
import com.example.EdufyVideo.models.dtos.RegisterMediaGenreDTO;
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


//ED-61-AA
@Service
public class CreatorClient {

    private final RestClient restClient;

    public CreatorClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://gateway:4545/api/v1/creator").build();
    }

    public CreatorDTO getCreatorById(Long creatorId) {
        try {
            return restClient.get()
                    .uri("/creator/{id}", creatorId)
                    .retrieve()
                    .body(CreatorDTO.class);
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }

    //ED-61-AA (Get CreatorDTO with creator ID and a list of MeidaIds
    public CreatorDTO getCreatorWithMediaList(Long creatorId, MediaType mediaType) {
        try {
            return restClient.get()
                    .uri("/creator/{mediaType}/{id}", mediaType, creatorId)//TODO update url after method is done.
                    .retrieve()
                    .body(CreatorDTO.class);
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }


    public List<CreatorDTO> getCreatorsByMediaTypeAndMediaId(MediaType mediaType, long mediaId) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/creator/creators-media") //TODO update url after method is done.
                            .queryParam("mediaType", mediaType.name())
                            .queryParam("mediaId", mediaId)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CreatorDTO>>() {
                    });
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }

    //ED-243-AA
    public boolean createRecordeOfMedia(MediaType mediaType, Long mediaId, List<Long> creatorIds) {
        try {
            ResponseEntity<Void> response = restClient.post()
                    .uri("/media/record")
                    .body(new RegisterMediaCreatorDTO(mediaId, mediaType, creatorIds))
                    .retrieve()
                    .toBodilessEntity();

            return response.getStatusCode() == HttpStatus.CREATED;

        } catch (RestClientResponseException ex) {
            // Client Call returns 400/404/409/500
            String error = ex.getResponseBodyAsString();
            throw new InvalidInputException("Creator-service error: " + error);

        } catch (ResourceAccessException ex) {
            //Can not reach Thumb at all
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }

    //ED-277-AA – Helper to return usernames directly
    public List<String> getCreatorUsernamesByMedia(MediaType mediaType, long mediaId) {
        try {
            List<CreatorDTO> creators = getCreatorsByMediaTypeAndMediaId(mediaType, mediaId);

            if (creators == null || creators.isEmpty()) {
                return List.of("CREATOR UNKNOWN");
            }

            return creators.stream()
                    .map(CreatorDTO::getUsername)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException e) {
            return List.of("CREATOR UNKNOWN");
        }
    }

    //ED-277-AA – Helper to return combined "id - username" strings
    public List<String> getCreatorIdAndUsernameByMedia(MediaType mediaType, long mediaId) {
        try {
            List<CreatorDTO> creators = getCreatorsByMediaTypeAndMediaId(mediaType, mediaId);

            if (creators == null || creators.isEmpty()) {
                return List.of("CREATOR UNKNOWN");
            }

            return creators.stream()
                    .map(c -> c.getId() + " - " + c.getUsername())
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException e) {
            return List.of("CREATOR UNKNOWN");
        }
    }

}
