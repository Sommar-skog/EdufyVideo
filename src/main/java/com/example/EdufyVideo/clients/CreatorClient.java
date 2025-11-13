package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.enums.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



//ED-61-AA
@Service
public class CreatorClient {

    private final RestClient restClient;

    public CreatorClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://EDUFYCREATOR").build();
    }

    public CreatorDTO getCreatorWithMediaLists(Long creatorId) {
        return restClient.get()
                .uri("/creator/creator/{id}", creatorId)
                .retrieve()
                .body(CreatorDTO.class);
    }

    public CreatorDTO getCreatorsByMediaTypeAndMediaId(MediaType mediaType, long mediaId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/creator/creators-media")
                        .queryParam("mediaType", mediaType.name()) // ENUM → String
                        .queryParam("mediaId", mediaId)
                        .build())
                .retrieve()
                .body(CreatorDTO.class);
    }

}
