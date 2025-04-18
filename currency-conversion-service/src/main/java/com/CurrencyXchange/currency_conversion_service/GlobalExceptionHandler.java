package com.CurrencyXchange.currency_conversion_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        return createErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleRestClientException(
            RestClientException ex, WebRequest request) {

        return createErrorResponse(
                ex,
                request,
                HttpStatus.SERVICE_UNAVAILABLE,
                "Service Unavailable",
                "Currency exchange service is currently unavailable. Please try again later."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtExceptions(
            Exception ex, WebRequest request) {

        return createErrorResponse(
                ex,
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later."
        );
    }

    private ResponseEntity<Object> createErrorResponse(
            Exception ex,
            WebRequest request,
            HttpStatus status,
            String error,
            String message) {

        logger.error("{}: {}", error, ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, status);
    }
}
