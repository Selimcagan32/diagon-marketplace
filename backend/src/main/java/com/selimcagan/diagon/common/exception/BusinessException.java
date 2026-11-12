package com.selimcagan.diagon.common.exception;

/**
 * Bir iş kuralı çiğnendiğinde fırlatılır (örneğin stok yetersiz).
 * HTTP 409 olarak döner.
 */
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
