package com.selimcagan.diagon.common.exception;

/**
 * Aranan kayıt bulunamadığında fırlatılır. HTTP 404 olarak döner.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
