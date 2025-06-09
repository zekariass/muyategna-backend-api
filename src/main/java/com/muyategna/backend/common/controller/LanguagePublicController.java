package com.muyategna.backend.common.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/common/languages")
@Tag(name = "Language Public Controller", description = "Language Public Controller")
public class LanguagePublicController {
    private final LanguageService languageService;

    public LanguagePublicController(LanguageService languageService) {
        this.languageService = languageService;
    }

    /**
     * Get all languages for the current country.
     *
     * @param request the HTTP request
     * @return a response entity containing the list of languages
     */
    @GetMapping
    @Operation(summary = "Get all languages for current country", description = "Get all languages for current country")
    public ResponseEntity<ApiResponse<List<LanguageDto>>> getAllLanguagesByCountry(HttpServletRequest request) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        List<LanguageDto> languages = languageService.getAllLanguagesByCountryPublic(countryDto).stream().toList();

        ApiResponse<List<LanguageDto>> response = ApiResponse.<List<LanguageDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Languages for current country fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(languages)
                .build();

        return ResponseEntity.ok(response);
    }
}
