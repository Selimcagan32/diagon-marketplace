package com.selimcagan.diagon.identity;

import com.selimcagan.diagon.common.api.ApiResponse;
import com.selimcagan.diagon.common.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Giriş yapmış kullanıcının kendi adresleri.
 */
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ApiResponse<List<AddressResponse>> list(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.ok(addressService.list(userId(jwt)));
    }

    @GetMapping("/{id}")
    public ApiResponse<AddressResponse> get(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        return ApiResponse.ok(addressService.get(userId(jwt), id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AddressResponse> create(@AuthenticationPrincipal Jwt jwt,
                                               @Valid @RequestBody AddressRequest request) {
        return ApiResponse.ok(addressService.create(userId(jwt), request));
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressResponse> update(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
                                               @Valid @RequestBody AddressRequest request) {
        return ApiResponse.ok(addressService.update(userId(jwt), id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        addressService.delete(userId(jwt), id);
    }

    /** Kullanıcı kimliği her zaman token'dan alınır, istekten değil. */
    private UUID userId(Jwt jwt) {
        return CurrentUser.from(jwt).id();
    }
}
