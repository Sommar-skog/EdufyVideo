package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.RegisterMediaThumbDTO;
import com.example.EdufyVideo.models.enums.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

//ED-243-AA
@Service
public class ThumbClientImpl {

    private final RestClient restClient;
    private final Keycloak keycloak;

    public ThumbClientImpl(RestClient.Builder builder, Keycloak keycloak) {

        this.restClient = builder.baseUrl("http://gateway:4545/api/v1/thumb").build();
        this.keycloak = keycloak;
    }

    public boolean createRecordeOfMedia(MediaType mediaType, Long mediaId, String mediaName) {
        try {
             ResponseEntity<Void> response= restClient.post()
                    .uri("/media/record")
                    .body(new RegisterMediaThumbDTO(mediaId,mediaType,mediaName))
                     .header("Authorization", "Bearer " + keycloak.getAccessToken())
                    .retrieve()
                    .toBodilessEntity();

            return response.getStatusCode() == HttpStatus.CREATED;

        } catch (RestClientResponseException ex) {
            // Client Call returns 400/404/409/500
            String error = ex.getResponseBodyAsString();
            throw new InvalidInputException("Thumb-service error: " + error);

        } catch (ResourceAccessException ex) {
            //Can not reach Thumb at all
            throw new RestClientException("EdufyVideo", "EdufyThumb");
        }
    }

}
