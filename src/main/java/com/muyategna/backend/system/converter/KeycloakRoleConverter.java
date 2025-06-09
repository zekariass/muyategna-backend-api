package com.muyategna.backend.system.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converts Keycloak roles from JWT token to Spring Security GrantedAuthority.
 * <p>
 * This class implements the Converter interface to convert JWT tokens into a collection of GrantedAuthority.
 * It extracts the roles from the "realm_access" claim in the JWT token and prefixes them with "ROLE_".
 * </p>
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Converts a JWT token to a collection of GrantedAuthority.
     *
     * @param source the JWT token containing roles in the "realm_access" claim
     * @return a collection of GrantedAuthority representing the roles
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return returnValue;
    }
}
