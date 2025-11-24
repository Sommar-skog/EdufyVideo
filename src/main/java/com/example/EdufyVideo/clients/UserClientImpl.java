package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.exceptions.RestClientException;
import com.example.EdufyVideo.models.dtos.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

//ED-282-AA
@Service
public class UserClientImpl {

    private final RestClient restClient;

    public UserClientImpl(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://gateway:4545/api/v1/user").build();
    }

    public UserDTO getUserBySub(String sub) {
        try {
            return restClient.get()
                    .uri("/user-sub/{sub}/clientcall", sub)
                    .retrieve()
                    .body(UserDTO.class);
        } catch (Exception e) {
            throw new RestClientException("EdufyVideo", "EdufyUser");
        }
    }
}
