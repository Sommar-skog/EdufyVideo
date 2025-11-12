package com.example.EdufyVideo.configs;

import com.example.EdufyVideo.converters.JwtAuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //ED-167-AA
    private JwtAuthConverter jwtAuthConverter;

    //ED-167-AA
    @Autowired
    public void setJwtAuthConverter(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    //ED-41-AA
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                 .csrf(csrf -> csrf.disable())
                 .headers(h -> h.frameOptions(f -> f.disable()))
                 .authorizeHttpRequests(auth ->
                         auth
                                 //.requestMatchers("/video/**").authenticated() //ED-252-AA
                                 .requestMatchers("/h2-console/**").permitAll()
                                 .anyRequest().authenticated()
                 )  //ED-225-AA
                 .oauth2ResourceServer(oauth2 ->
                         oauth2
                                 .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                 );
        return http.build();
    }
}
