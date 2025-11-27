package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.MediaDTO;
import com.example.EdufyVideo.models.dtos.RegisterMediaCreatorDTO;
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
public class CreatorClientImpl implements CreatorClient {

    private final RestClient restClient;
    private final Keycloak keycloak;

        public CreatorClientImpl(RestClient.Builder builder, Keycloak keycloak) {
            this.restClient = builder.baseUrl("http://gateway:4545/api/v1/creator").build();
            this.keycloak = keycloak;
        }

    public CreatorDTO getCreatorById(Long creatorId) {
        try {
            return restClient.get()
                    .uri("/creator/{id}/clientcall", creatorId)
                    .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .body(CreatorDTO.class);
        } catch (RestClientResponseException ex) {
            // Client Call returns 400/404/409/500
            String error = ex.getResponseBodyAsString();
            throw new InvalidInputException("Creator-service error: " + error);

        } catch (ResourceAccessException ex) {
            //Can not reach Creators at all
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }

    //ED-61-AA //Get list of DTOs with just mediaId //ED-345-AA
    public List<MediaDTO> getMediaListFromCreator(Long creatorId, MediaType mediaType) {
        try {
            return restClient.get()
                    .uri("/mediabycreator/{creatorId}/{mediaType}", creatorId, mediaType)
                    .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<MediaDTO>>() {});
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }


    public List<CreatorDTO> getCreatorsByMediaTypeAndMediaId(MediaType mediaType, long mediaId) {
        System.out.println(mediaId  +" "+mediaType.name());
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/creators-mediaid")
                            .queryParam("mediaType", mediaType.name())
                            .queryParam("id", mediaId)
                            .build())
                    .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CreatorDTO>>() {
                    });
        } catch (RestClientResponseException ex) {
            // Client Call returns 400/404/409/500
            String error = ex.getResponseBodyAsString();
            throw new InvalidInputException("Creator-service error: " + error);

        } catch (ResourceAccessException ex) {
            //Can not reach Creators at all
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }

    //ED-243-AA
    public boolean createRecordeOfMedia(MediaType mediaType, Long mediaId, List<Long> creatorIds) {
        try {
            ResponseEntity<Void> response = restClient.put()
                    .uri("/media/record")
                    .body(new RegisterMediaCreatorDTO(mediaId, mediaType, creatorIds))
                    .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .toBodilessEntity();

            return response.getStatusCode() == HttpStatus.OK;

        } catch (RestClientResponseException ex) {
            // Client Call returns 400/404/409/500
            String error = ex.getResponseBodyAsString();
            throw new InvalidInputException("Creator-service error: " + error);

        } catch (ResourceAccessException ex) {
            //Can not reach Creator at all
            throw new RestClientException("EdufyVideo", "EdufyCreator");
        }
    }

    //ED-277-AA – Helper to return usernames directly
    public List<String> getCreatorUsernamesByMedia(MediaType mediaType, long mediaId) {
        try {
            List<CreatorDTO> creators = getCreatorsByMediaTypeAndMediaId(mediaType, mediaId);

            if (creators == null || creators.isEmpty()) {
                return List.of("CREATOR UNKNOWN ");
            }

            return creators.stream()
                    .map(CreatorDTO::getUsername)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException e) {
            return List.of("CREATOR UNKNOWN " + e.getMessage());
        }
    }

    //ED-277-AA – Helper to return combined "id - username" strings
    public List<String> getCreatorIdAndUsernameByMedia(MediaType mediaType, long mediaId) {
        try {
            List<CreatorDTO> creators = getCreatorsByMediaTypeAndMediaId(mediaType, mediaId);
            System.out.println(creators);

            if (creators == null || creators.isEmpty()) {
                return List.of("CREATOR UNKNOWN");
            }

            return creators.stream()
                    .map(c -> c.getId() + " - " + c.getUsername())
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException e) {
            return List.of("CREATOR UNKNOWN" + e.getMessage());
        }
    }

}
