package com.selimcagan.diagon.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * Hata bilgisi: makinenin okuyacağı kod, kullanıcının okuyacağı mesaj
 * ve varsa alan bazlı doğrulama hataları.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String code, String message, Map<String, String> fieldErrors) {

    public static ApiError of(String code, String message) {
        return new ApiError(code, message, null);
    }
}
