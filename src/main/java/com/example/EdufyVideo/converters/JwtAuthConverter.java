package com.example.EdufyVideo.converters;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//ED-167-AA
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${video.client.id}")
    private String clientId;

    //ED-167-AA
    @Override
    public AbstractAuthenticationToken convert(@Nonnull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                getAuthorities(jwt).stream())
                .collect(Collectors.toSet()
        );
        return new JwtAuthenticationToken(jwt, authorities);
    }

    //ED-167-AA
    private Collection<? extends GrantedAuthority> getAuthorities(Jwt jwt) {
        Collection<String> resourceRoles;
        Map<?, ?> resourceAccess;
        Map<?, ?> resource;

        if(!jwt.hasClaim("resource_access")) {
            return Collections.emptySet();
        }

        resourceAccess = jwt.getClaimAsMap("resource_access");

        if (!resourceAccess.containsKey(clientId)) {
            return  Collections.emptySet();
        }

        resource = (Map<?, ?>) resourceAccess.get(clientId);

        if (!resource.containsKey("roles")) {
            return Collections.emptySet();
        }

        resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());

    }
}
