package com.example.EdufyVideo.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

//ED-282-AA
@Service
public class UserClient {
    private final RestClient restClient;

    public UserClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://EDUFYUSER").build();
    }
}
