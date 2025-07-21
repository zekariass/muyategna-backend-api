package com.muyategna.backend.system.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyategna.backend.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType("application/json;charset=UTF-8");
//
//        ApiResponse<Object> apiResponse = ApiResponse.builder()
//                .success(false)
//                .statusCode(HttpStatus.UNAUTHORIZED.value())
//                .message(authException.getMessage())
//                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
//                .path(request.getRequestURI())
//                .build();
//
//        ObjectMapper mapper = new ObjectMapper();
//        response.getWriter().write(mapper.writeValueAsString(apiResponse));
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Add CORS headers manually
        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            response.setHeader("Access-Control-Allow-Origin", "*"); // Optional fallback
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Vary", "Origin"); // Helps prevent caching CORS headers for different origins

        // Now send your 401 JSON response
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .success(false)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(authException.getMessage())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .path(request.getRequestURI())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }

}
