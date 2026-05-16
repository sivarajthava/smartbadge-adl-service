package com.smartbadge.adl.shared.exception;

import com.smartbadge.adl.shared.ApiResponse;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";
    private static final String INTERNAL_SERVER_ERROR = "500";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(StaffAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleStaffAlreadyExists(StaffAlreadyExistsException ex) {
        log.warn("Staff already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
        log.warn("Duplicate resource: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST, errors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingRequestParameter(MissingServletRequestParameterException ex) {
        String message = String.format("Missing required parameter: %s", ex.getParameterName());
        log.warn("Missing request parameter: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST, message));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingPathVariable(MissingPathVariableException ex) {
        String message = String.format("Missing required path variable: %s", ex.getVariableName());
        log.warn("Missing path variable: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST, message));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        String message = String.format("Missing required header: %s", ex.getHeaderName());
        log.warn("Missing request header: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST, message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST, msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(INTERNAL_SERVER_ERROR, "Unable to process your request"));
    }
}
