package com.muyategna.backend.location.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.country.CountryLocalizedDto;
import com.muyategna.backend.location.service.CountryService;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.context.LanguageContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/location/countries")
@Slf4j
@Tag(name = "Country Public Controller", description = "Endpoints related to country public access")
public class CountryPublicController {

    private final CountryService countryService;

    @Autowired
    public CountryPublicController(CountryService countryService) {
        this.countryService = countryService;
    }


    /**
     * Handles HTTP GET request to retrieve the current user's country.
     *
     * @param request the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the current user's CountryLocalizedDto entity as an EntityModel
     */
    @GetMapping("/current")
    @Operation(summary = "Get current user country", description = "Get current user country")
    public ResponseEntity<ApiResponse<CountryLocalizedDto>> getCurrentUserCountry(HttpServletRequest request) {

        CountryDto countryDto = CountryContextHolder.getCountry();
        LanguageDto languageDto = LanguageContextHolder.getLanguage();

        CountryLocalizedDto currentCountryDto = countryService.getCurrentCountryPublic(countryDto, languageDto);
        ApiResponse<CountryLocalizedDto> response = ApiResponse.<CountryLocalizedDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Languages for current country fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(currentCountryDto)
                .build();
        return ResponseEntity.ok(response);
    }
}
