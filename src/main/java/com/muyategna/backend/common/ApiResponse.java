package com.muyategna.backend.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/*
 * ApiResponse is a generic class that represents the structure of an API response.
 * It includes fields for success status, HTTP status code, message, error details,
 * validation errors, request path, data payload, and timestamp.
 *
 * @param <T> the type of the data payload
 */
@Setter
@Getter
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private String error;
    private Map<String, String> errors;
    private String path;
    private T data;
    private LocalDateTime timestamp;

}
