package com.selimcagan.diagon.common.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Giriş yapmış kullanıcının token'dan okunan bilgileri.
 * "id" Keycloak'taki kullanıcı kimliğidir (sub); kullanıcıya ait her kayıt bu kimlikle saklanır.
 */
public record CurrentUser(UUID id, String email, String firstName, String lastName, List<String> roles) {

    /** Uygulamanın kendi rolleri; Keycloak'ın teknik rolleri (offline_access gibi) dışarıda kalır. */
    private static final Set<String> APP_ROLES = Set.of("customer", "seller", "admin");

    public static CurrentUser from(Jwt jwt) {
        List<String> roles = KeycloakRoleConverter.realmRoles(jwt).stream()
                .filter(APP_ROLES::contains)
                .sorted()
                .toList();
        return new CurrentUser(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                roles);
    }
}
