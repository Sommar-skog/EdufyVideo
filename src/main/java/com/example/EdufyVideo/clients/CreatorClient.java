package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.models.dtos.CreatorDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

//ED-61-AA
@Service
public class CreatorClient {

    private final RestClient restClient;

    public CreatorClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://EDUFYCREATOR").build();
    }

    public CreatorDTO getClipIdsByCreator(Long creatorId) {
        return restClient.get()
                .uri("/creator/creator/{id}", creatorId)
                .retrieve()
                .body(CreatorDTO.class);
    }

}
