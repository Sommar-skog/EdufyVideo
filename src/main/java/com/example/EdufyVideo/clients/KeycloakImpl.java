package com.example.EdufyVideo.clients;


import com.example.EdufyVideo.models.tokens.TokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


//ED-345-AA
@Component
public class KeycloakImpl implements Keycloak {

    private final RestClient restClient;
    private final String keycloakUrl;
    private final String clientSecret;
    private final String clientId;

    //ED-345-AA
    public KeycloakImpl(RestClient.Builder restClientBuilder,
                        @Value("${keycloak.url}") String keycloakUrl,
                        @Value("${video.client.id}") String clientId,
                        @Value("${keycloak.client-secret}") String clientSecret) {
        this.restClient = restClientBuilder.build();
        this.keycloakUrl = keycloakUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    //ED-345-AA
    @Override
    public String getAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        TokenResponse tokenResponse = restClient
                .post()
                .uri(keycloakUrl + "/realms/edufy_realm/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(TokenResponse.class);

        assert tokenResponse != null;
        return tokenResponse.accessToken();
    }
}
