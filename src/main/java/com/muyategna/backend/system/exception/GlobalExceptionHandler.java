package com.muyategna.backend.system.exception;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.user.exception.DuplicateUserException;
import com.muyategna.backend.user.exception.UserCreationException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.LazyInitializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler handles global exceptions across the application.
 * It provides a centralized mechanism for converting exceptions into error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles RuntimeException and returns a ResponseEntity with an ApiResponse.
     *
     * @param ex      the RuntimeException
     * @param request the HttpServletRequest
     * @return a ResponseEntity with an ApiResponse
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    /**
     * Handles IllegalArgumentException and returns a ResponseEntity with an ApiResponse.
     *
     * @param ex      the IllegalArgumentException
     * @param request the HttpServletRequest
     * @return a ResponseEntity with an ApiResponse
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles NullPointerException and returns a ResponseEntity with an ApiResponse.
     *
     * @param ex      the NullPointerException
     * @param request the HttpServletRequest
     * @return a ResponseEntity with an ApiResponse
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Null pointer exception occurred", request);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a ResponseEntity with an ApiResponse.
     *
     * @param ex      the MethodArgumentNotValidException
     * @param request the HttpServletRequest
     * @return a ResponseEntity with an ApiResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildApiResponse(errors, HttpStatus.BAD_REQUEST, "Validation failed", request);
    }


    @ExceptionHandler(LazyInitializationException.class)
    public ResponseEntity<ApiResponse<Object>> handleLazyInitializationException(LazyInitializationException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles all other exceptions and returns a ResponseEntity with an ApiResponse.
     *
     * @param ex      the Exception
     * @param request the HttpServletRequest
     * @return a ResponseEntity with an ApiResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    /**
     * Handles RoleDoesNotFoundException and returns a ResponseEntity with an ApiResponse.
     *
     * @param ex      the RoleDoesNotFoundException
     * @param request the HttpServletRequest
     * @return a ResponseEntity with an ApiResponse
     */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiResponse<Object>> duplicateUserException(DuplicateUserException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.CONFLICT, ex.getMessage(), request);
    }


    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<ApiResponse<Object>> dataIntegrityViolation(DataIntegrityException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles UserCreationException and returns a ResponseEntity with an ApiResponse.
     *
     * @param ex      the UserCreationException
     * @param request the HttpServletRequest
     * @return a ResponseEntity with an ApiResponse
     */
    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserCreationException(UserCreationException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidLanguageCodeException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidLanguageCodeException(InvalidLanguageCodeException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(UserDoesNotHaveDefaultLanguageException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserDoesNotHaveDefaultLanguageException(UserDoesNotHaveDefaultLanguageException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return buildApiResponse(null, HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }


    private ResponseEntity<ApiResponse<Object>> buildApiResponse(Map<String, String> errors, HttpStatus status, String message, HttpServletRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .statusCode(status.value())
                .error(status.getReasonPhrase())
                .errors(errors)
                .message(message)
                .path(request.getRequestURI())
                .success(false)
                .timestamp(LocalDateTime.now())
                .build();


        return new ResponseEntity<>(apiResponse, status);
    }
}
