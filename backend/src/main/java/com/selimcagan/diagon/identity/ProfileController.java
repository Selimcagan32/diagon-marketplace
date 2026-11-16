package com.selimcagan.diagon.identity;

import com.selimcagan.diagon.common.api.ApiResponse;
import com.selimcagan.diagon.common.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Giriş yapmış kullanıcının profil bilgisi.
 * Ad, e-posta ve roller Keycloak'ta tutulur; burada token'dan okunur.
 */
@RestController
@RequestMapping("/api/v1/me")
public class ProfileController {

    @GetMapping
    public ApiResponse<CurrentUser> me(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.ok(CurrentUser.from(jwt));
    }
}
