package com.example.EdufyVideo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                                 .requestMatchers("/video/**").authenticated() //ED-252-AA
                                 .requestMatchers("/h2-console/**").permitAll()
                                 .anyRequest().permitAll() //change later
                 )   .httpBasic(Customizer.withDefaults()); //TODO remove httpBasic when connected to Keycloak
                /* .oauth2ResourceServer(oauth2 ->
                         oauth2
                                 .jwt(jwt -> jwt.jwtAuthenticationConverter())
                 );*/
        return http.build();
    }


    //TODO Until kecloak is connected for testing endpoints with roles then remove
    @Bean
    public UserDetailsService userDetailsService() {
        var user1 = User.withUsername("user").password("{noop}1234").roles("video_user").build();
        var admin = User.withUsername("admin").password("{noop}admin").roles("video_admin").build();
        return new InMemoryUserDetailsManager(user1, admin);
    }
}
