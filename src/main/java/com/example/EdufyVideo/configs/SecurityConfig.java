package com.example.EdufyVideo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //add converter

    //ED-41-AA
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                 .csrf(csrf -> csrf.disable())
                 .headers(h -> h.frameOptions(f -> f.disable()))
                 .authorizeHttpRequests(auth ->
                         auth
                                 .requestMatchers("/h2-console/**").permitAll()
                                 .anyRequest().permitAll() //change later
                 );
                /* .oauth2ResourceServer(oauth2 ->
                         oauth2
                                 .jwt(jwt -> jwt.jwtAuthenticationConverter())
                 );*/
        return http.build();
    }
}
