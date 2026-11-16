package com.selimcagan.diagon.common.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Keycloak token'ındaki "realm_access.roles" listesini Spring'in yetkilerine çevirir.
 * Örnek: "admin" rolü "ROLE_ADMIN" yetkisi olur.
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return realmRoles(jwt).stream()
                .map(role -> ROLE_PREFIX + role.toUpperCase(Locale.ROOT))
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
    }

    /** Token'daki realm rollerini Keycloak'taki adlarıyla döndürür (örneğin "admin"). */
    public static List<String> realmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null || !(realmAccess.get("roles") instanceof Collection<?> roles)) {
            return List.of();
        }
        return roles.stream()
                .map(Object::toString)
                .toList();
    }
}