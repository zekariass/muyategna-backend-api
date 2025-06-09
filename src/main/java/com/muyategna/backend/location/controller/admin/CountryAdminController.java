package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.location.assembler.CountryDtoModelAdminAssembler;
import com.muyategna.backend.location.dto.country.CountryCreateDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.country.CountryUpdateDto;
import com.muyategna.backend.location.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/location/countries")
@PreAuthorize("isAuthenticated() && hasAuthority('ROLE_ADMIN')")
@Tag(name = "Country Admin Management", description = "Country Admin Controller")
public class CountryAdminController {

    private final CountryService countryService;
    private final CountryDtoModelAdminAssembler countryDtoAssembler;

    @Autowired
    public CountryAdminController(CountryService countryService, CountryDtoModelAdminAssembler countryDtoAssembler) {
        this.countryService = countryService;
        this.countryDtoAssembler = countryDtoAssembler;
    }


    /**
     * Handles HTTP POST request for adding a new country.
     *
     * @param createDto the data transfer object containing the details of the country to be created.
     * @param request   the HttpServletRequest object containing the request details.
     * @return a ResponseEntity containing an ApiResponse with the saved CountryDto as an EntityModel.
     */
    @Operation(summary = "Add new country", description = "Add new country")
    @PostMapping
    public ResponseEntity<ApiResponse<EntityModel<CountryDto>>> addNewCountry(@Parameter(description = "Country to be saved", required = true) @Valid @RequestBody CountryCreateDto createDto,
                                                                              HttpServletRequest request) {

        CountryDto savedCountry = countryService.saveCountry(createDto);

        EntityModel<CountryDto> countryModel = countryDtoAssembler.toModel(savedCountry, request);

        ApiResponse<EntityModel<CountryDto>> response = ApiResponse.<EntityModel<CountryDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Country saved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Handles HTTP GET request to retrieve all countries.
     *
     * @param request the HttpServletRequest object containing request details.
     * @return a ResponseEntity containing an ApiResponse with a collection of CountryDto entities as EntityModels.
     * The response includes all countries, optionally with their translations.
     */
    @GetMapping
    @Operation(summary = "Get all countries", description = "Get all countries with or without translations")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<CountryDto>>>> getAllCountries(HttpServletRequest request) {

        List<CountryDto> countriesDto = countryService.getAllCountries();

        CollectionModel<EntityModel<CountryDto>> countriesCollectionModel = countryDtoAssembler.toCollectionModel(countriesDto, request);

        ApiResponse<CollectionModel<EntityModel<CountryDto>>> response = ApiResponse.<CollectionModel<EntityModel<CountryDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Countries retrieved successfully with or without translations")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countriesCollectionModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Handles HTTP GET request to retrieve a country by its ID.
     *
     * @param countryId the ID of the country to be retrieved
     * @param request   the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the requested CountryDto entity as an EntityModel
     * The response includes the country details if found, otherwise an appropriate error response
     */
    @GetMapping("/{countryId}")
    @Operation(summary = "Get country by id", description = "Get country by id")
    public ResponseEntity<ApiResponse<EntityModel<CountryDto>>> getCountryById(@Parameter(description = "Id of the country to be retrieved", required = true) @PathVariable Long countryId, HttpServletRequest request) {

        CountryDto countryDto = countryService.getCountryById(countryId);

        EntityModel<CountryDto> countryModel = countryDtoAssembler.toModel(countryDto, request);

        ApiResponse<EntityModel<CountryDto>> response = ApiResponse.<EntityModel<CountryDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country with id: %d is retrieved successfully".formatted(countryId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Handles HTTP GET request to retrieve a country by its ISO 3166-1 alpha-2 code.
     *
     * @param isoCode2 the ISO 3166-1 alpha-2 code of the country to be retrieved
     * @param request  the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the requested CountryDto entity as an EntityModel
     * The response includes the country details if found, otherwise an appropriate error response
     */
    @GetMapping("/iso-code-2/{isoCode2}")
    @Operation(summary = "Get country by iso code 2", description = "Get country by iso code 2")
    public ResponseEntity<ApiResponse<EntityModel<CountryDto>>> getCountryByIsoCode2(@Parameter(description = "Iso code 2 of the country to be retrieved", required = true) @PathVariable String isoCode2, HttpServletRequest request) {

        CountryDto countryDto = countryService.getCountryByIsoCode2(isoCode2);

        EntityModel<CountryDto> countryModel = countryDtoAssembler.toModel(countryDto, request);

        ApiResponse<EntityModel<CountryDto>> response = ApiResponse.<EntityModel<CountryDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country with iso code 2: %s is retrieved successfully".formatted(isoCode2))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Handles HTTP PUT request to update a country by its id.
     *
     * @param countryId the id of the country to be updated
     * @param updateDto the data transfer object containing the details of the country to be updated
     * @param request   the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the updated CountryDto entity as an EntityModel
     * The response includes the updated country details if found, otherwise an appropriate error response
     */
    @PutMapping("/{countryId}")
    @Operation(summary = "Update country by id", description = "Update country by id")
    public ResponseEntity<ApiResponse<EntityModel<CountryDto>>> updateCountryById(@Parameter(description = "Id of the country to be updated", required = true) @PathVariable Long countryId,
                                                                                  @Parameter(description = "Country to be updated", required = true) @Valid @RequestBody CountryUpdateDto updateDto,
                                                                                  HttpServletRequest request) {

        CountryDto countryDto = countryService.updateCountry(countryId, updateDto);

        EntityModel<CountryDto> countryModel = countryDtoAssembler.toModel(countryDto, request);

        ApiResponse<EntityModel<CountryDto>> response = ApiResponse.<EntityModel<CountryDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country with id: %d is updated successfully".formatted(countryId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryModel)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Handles HTTP DELETE request to delete a country by its id.
     *
     * @param countryId the id of the country to be deleted
     * @param request   the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a success message, or an appropriate error response
     */
    @DeleteMapping("/{countryId}")
    @Operation(summary = "Delete country by id", description = "Delete country by id")
    public ResponseEntity<ApiResponse<Void>> deleteCountryById(@Parameter(description = "Id of the country to be deleted", required = true) @PathVariable Long countryId, HttpServletRequest request) {

        countryService.deleteCountry(countryId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country with id: %d is deleted successfully".formatted(countryId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

}
