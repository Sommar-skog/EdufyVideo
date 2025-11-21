package com.example.EdufyVideo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor((req, body, exec) -> {
                    var auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth instanceof JwtAuthenticationToken jwtAuth) {
                        req.getHeaders().setBearerAuth(jwtAuth.getToken().getTokenValue());
                    }
                    return exec.execute(req, body);
                })
                .build();
    }
}
