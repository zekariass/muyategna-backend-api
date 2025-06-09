package com.muyategna.backend.system.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.service.CountryService;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CountryFilter extends OncePerRequestFilter {

    private final CountryService countryService;

    public CountryFilter(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Determines whether the request should be filtered.
     *
     * <p>This method checks if the request URI matches any of the public API paths
     * defined in the application. If the request URI starts with any of these paths,
     * the request will not be filtered by this filter.</p>
     *
     * @param request the HTTP request to be checked
     * @return {@code true} if the request should not be filtered, {@code false} otherwise
     * @throws ServletException if an error occurs during the filtering process
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Constants.PUBLIC_API_PATHS_FOR_FILTER.stream().anyMatch(path::startsWith);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            CountryDto resolvedCountry = null;

            // Get country code from the request header
            String countryIsoCode2 = request.getHeader("X-Country-Code");

            // If the header is not null or empty, get the country by ISO code from the database
            if (countryIsoCode2 != null && !countryIsoCode2.isEmpty()) {
                resolvedCountry = countryService.getCountryByIsoCode2(countryIsoCode2);
                if (resolvedCountry == null) {
                    throw new ResourceNotFoundException("Country not found for ISO code " + countryIsoCode2);
                }
            }

            // Set the resolved country in the CountryContextHolder
            CountryContextHolder.setCountry(resolvedCountry);
            log.info("Resolved country of request is: {}", resolvedCountry);

            // Proceed with the request
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.error("Error setting language context for request: {}", request.getRequestURI(), e);

            // Format and write the error response
            if (!response.isCommitted()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                ApiResponse<Object> apiResponse = ApiResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .error(e.getClass().getSimpleName())
                        .success(false)
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .build();

                try {
                    String json = new ObjectMapper().writeValueAsString(apiResponse);
                    response.getWriter().write(json);
                    response.getWriter().flush(); // Force the response to be sent immediately
                } catch (IOException ioException) {
                    log.error("Failed to write error response", ioException);
                }
            } else {
                log.warn("Response already committed, could not write error body.");
            }
        } finally {
            CountryContextHolder.clear();
        }
    }
}
