package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.UserDTO;
import com.example.EdufyVideo.models.enums.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

//ED-243-AA
@Service
public class ThumbClient {

    private final RestClient restClient;

    public ThumbClient(RestClient.Builder builder) {

        this.restClient = builder.baseUrl("http://EDUFYTHUB;").build();
    }

    public void createRecordeOfMedia(MediaType mediaType, Long mediaId) {
        try {
            return restClient.post()
                    .uri("/thumb/media/record")
                    .body()
                    .retrieve()
                    .body(UserDTO.class);
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyThumb");
        }
    }

}
