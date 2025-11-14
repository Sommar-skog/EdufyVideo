package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.enums.MediaType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;


//ED-61-AA
@Service
public class CreatorClient {

    private final RestClient restClient;

    public CreatorClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://EDUFYCREATOR").build();
    }

    public CreatorDTO getCreatorWithMediaLists(Long creatorId) {
        try {
            return restClient.get()
                    .uri("/creator/creator/{id}", creatorId)
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
                    .uri("/creator/creator/{mediaType}/{id}", mediaType, creatorId)
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
