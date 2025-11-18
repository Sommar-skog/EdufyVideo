package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.RegisterMediaThumbDTO;
import com.example.EdufyVideo.models.enums.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

//ED-243-AA
@Service
public class ThumbClient {

    private final RestClient restClient;

    public ThumbClient(RestClient.Builder builder) {

        this.restClient = builder.baseUrl("http://EDUFYTHUB;").build();
    }

    public void createRecordeOfMedia(MediaType mediaType, Long mediaId, String mediaName) {
        try {
             restClient.post()
                    .uri("/thumb/media/record")
                    .body(new RegisterMediaThumbDTO(mediaId,mediaType,mediaName))
                    .retrieve()
                    .toBodilessEntity();
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
