package com.muyategna.backend.location.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.region.RegionLocalizedDto;
import com.muyategna.backend.location.service.RegionService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/location/regions")
@Slf4j
@Tag(name = "Region Public Controller", description = "Endpoints related to region management")
public class RegionPublicController {
    private final RegionService regionService;

    @Autowired
    public RegionPublicController(RegionService regionService) {
        this.regionService = regionService;
    }


    /**
     * Get regions by country and language.
     *
     * @param request the HttpServletRequest object
     * @return a ResponseEntity containing a list of RegionLocalizedDto objects
     */
    @Operation(summary = "Get regions by country", description = "Get regions by country and language")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionLocalizedDto>>> getRegionsByCountry(HttpServletRequest request) {
        log.info("Retrieving regions for current user's country and language");
        CountryDto country = CountryContextHolder.getCountry();
        LanguageDto language = LanguageContextHolder.getLanguage();
        List<RegionLocalizedDto> localizedRegions = regionService.getAllRegionsByCountryAndLanguage(country, language);
        ApiResponse<List<RegionLocalizedDto>> response = ApiResponse.<List<RegionLocalizedDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(localizedRegions.isEmpty() ? "No regions found" : "Regions fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(localizedRegions)
                .build();
        log.info("Retrieved regions for current country countryId={}, languageId={}", country.getId(), language.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a region by its ID for the current user's country and language.
     *
     * @param regionId the ID of the region to be retrieved
     * @param request  the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a RegionLocalizedDto object
     * as the data, or a message indicating that the region was not found
     */
    @GetMapping("/{regionId}")
    @Operation(summary = "Get region by ID", description = "Get region by ID")
    public ResponseEntity<ApiResponse<RegionLocalizedDto>> getRegionByIdAndLanguage(Long regionId, HttpServletRequest request) {
        LanguageDto languageDto = LanguageContextHolder.getLanguage();
        CountryDto countryDto = CountryContextHolder.getCountry();
        RegionLocalizedDto region = regionService.getRegionByIdAndCountryAndLanguage(regionId, countryDto, languageDto);
        ApiResponse<RegionLocalizedDto> response = ApiResponse.<RegionLocalizedDto>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(region == null ? "Region not found" : "Region fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(region)
                .build();
        return ResponseEntity.ok(response);
    }
}
