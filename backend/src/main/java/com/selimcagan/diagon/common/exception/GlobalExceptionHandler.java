package com.selimcagan.diagon.common.exception;

import com.selimcagan.diagon.common.api.ApiError;
import com.selimcagan.diagon.common.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bütün controller'lardan çıkan hataları tek yerde yakalar
 * ve ortak hata formatına çevirir.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ApiError.of("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        return build(HttpStatus.CONFLICT, ApiError.of(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ApiError error = new ApiError("VALIDATION_ERROR", "Gönderilen veri geçersiz", fieldErrors);
        return build(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, ApiError.of("BAD_REQUEST", "İstek gövdesi okunamadı"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOther(Exception ex) {
        // Spring'in kendi hataları (olmayan adres, yanlış HTTP metodu gibi) durum kodunu yanında taşır.
        if (ex instanceof ErrorResponse errorResponse) {
            HttpStatusCode status = errorResponse.getStatusCode();
            HttpStatus resolved = HttpStatus.resolve(status.value());
            String code = resolved != null ? resolved.name() : "HTTP_" + status.value();
            return build(status, ApiError.of(code, errorResponse.getBody().getDetail()));
        }
        // Beklenmeyen hata: ayrıntı loga yazılır, kullanıcıya genel bir mesaj döner.
        log.error("Beklenmeyen hata", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                ApiError.of("INTERNAL_ERROR", "Beklenmeyen bir hata oluştu"));
    }

    private ResponseEntity<ApiResponse<Void>> build(HttpStatusCode status, ApiError error) {
        return ResponseEntity.status(status).body(ApiResponse.fail(error));
    }
}
