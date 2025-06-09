package com.muyategna.backend.location.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionLocalizedDto;
import com.muyategna.backend.location.service.SubCityOrDivisionService;
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
@RequestMapping("/api/v1/public/location/sub-cities-or-divisions")
@Tag(name = "Sub-City or Division Public Controller", description = "Public endpoints for sub-cities or divisions")
public class SubCityOrDivisionPublicController {
    private final SubCityOrDivisionService subCityOrDivisionService;

    @Autowired
    public SubCityOrDivisionPublicController(SubCityOrDivisionService subCityOrDivisionService) {
        this.subCityOrDivisionService = subCityOrDivisionService;
    }


    /**
     * Retrieves a sub-city or division by its ID and translates it to the current language.
     *
     * @param subCityOrDivisionId the ID of the sub-city or division to be retrieved
     * @param request             the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with a SubCityOrDivisionLocalizedDto object as the data
     */
    @GetMapping("/{subCityOrDivisionId}")
    @Operation(summary = "Get sub-city or division by id", description = "Get sub-city or division by id translated to the current language")
    public ResponseEntity<ApiResponse<SubCityOrDivisionLocalizedDto>> getSubCityOrDivisionById(@Parameter(description = "Id of the sub-city or division to be retrieved", required = true) @PathVariable Long subCityOrDivisionId,
                                                                                               HttpServletRequest request) {
        LanguageDto languageDto = LanguageContextHolder.getLanguage();
        SubCityOrDivisionLocalizedDto subCityOrDivisionDto = subCityOrDivisionService.getSubCityOrDivisionByIdPublic(subCityOrDivisionId, languageDto);

        ApiResponse<SubCityOrDivisionLocalizedDto> response = ApiResponse.<SubCityOrDivisionLocalizedDto>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(subCityOrDivisionDto == null ? "Region not found" : "Region fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(subCityOrDivisionDto)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a list of sub-cities or divisions for a given city id and translates them to the current language.
     *
     * @param cityId  Id of the city to be retrieved
     * @param request The HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with a list of SubCityOrDivisionLocalizedDto objects as the data
     */
    @GetMapping("/city/{cityId}")
    @Operation(summary = "Get sub-city or division by city id", description = "Get sub-city or division by city id translated to the current language")
    public ResponseEntity<ApiResponse<List<SubCityOrDivisionLocalizedDto>>> getSubCityOrDivisionByCityId(@Parameter(description = "Id of the city to be retrieved", required = true) @PathVariable Long cityId,
                                                                                                         HttpServletRequest request) {
        LanguageDto languageDto = LanguageContextHolder.getLanguage();
        List<SubCityOrDivisionLocalizedDto> subCityOrDivisionDto = subCityOrDivisionService.getSubCityOrDivisionByCityIdPublic(cityId, languageDto);

        ApiResponse<List<SubCityOrDivisionLocalizedDto>> response = ApiResponse.<List<SubCityOrDivisionLocalizedDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(subCityOrDivisionDto == null ? "Region not found" : "Region fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(subCityOrDivisionDto)
                .build();

        return ResponseEntity.ok(response);
    }
}
