package com.example.EdufyVideo.models.tokens;

import com.fasterxml.jackson.annotation.JsonProperty;

//ED-345-AA
public record TokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") long expiresIn) {
}
