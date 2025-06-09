package com.muyategna.backend.location.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.city.CityLocalizedDto;
import com.muyategna.backend.location.service.CityService;
import com.muyategna.backend.system.context.LanguageContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/location/cities")
@Tag(name = "City Public Controller", description = "City Public Controller")
public class CityPublicController {
    private final CityService cityService;

    @Autowired
    public CityPublicController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Retrieves a list of all cities by region ID.
     *
     * @param regionId the ID of the region
     * @param request  the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with a list of CityLocalizedDto objects as the data
     */
    @GetMapping("/region/{regionId}")
    @Operation(summary = "Get all cities by region", description = "Get all cities by region")
    public ResponseEntity<ApiResponse<List<CityLocalizedDto>>> getAllCitiesByRegion(@Parameter(description = "Id of the region", required = true) @PathVariable Long regionId, HttpServletRequest request) {
        LanguageDto languageDto = LanguageContextHolder.getLanguage();

        List<CityLocalizedDto> cities = cityService.getAllCitiesByRegion(regionId, languageDto.getId());

        ApiResponse<List<CityLocalizedDto>> response = ApiResponse.<List<CityLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Cities retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(cities)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a city by id and language.
     *
     * @param cityId  Id of the city to be retrieved
     * @param request The HTTP servlet request
     * @return A ResponseEntity containing an ApiResponse with a CityLocalizedDto object as the data
     */
    @GetMapping("/{cityId}")
    @Operation(summary = "Get city by id and language", description = "Get city by id and language")
    public ResponseEntity<ApiResponse<CityLocalizedDto>> getCityById(@Parameter(description = "Id of the city to be retrieved", required = true) @PathVariable Long cityId, HttpServletRequest request) {
        LanguageDto languageDto = LanguageContextHolder.getLanguage();

        CityLocalizedDto city = cityService.getCityById(cityId, languageDto);

        ApiResponse<CityLocalizedDto> response = ApiResponse.<CityLocalizedDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(city)
                .build();
        return ResponseEntity.ok(response);
    }

}
