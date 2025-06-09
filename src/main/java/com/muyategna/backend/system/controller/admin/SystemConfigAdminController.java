package com.muyategna.backend.system.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.AdminPaginationService;
import com.muyategna.backend.system.assembler.SystemConfigDtoModelAdminAssembler;
import com.muyategna.backend.system.dto.SystemConfigCreateDto;
import com.muyategna.backend.system.dto.SystemConfigDto;
import com.muyategna.backend.system.dto.SystemConfigUpdateDto;
import com.muyategna.backend.system.entity.SystemConfig;
import com.muyategna.backend.system.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin/system/config")
@PreAuthorize("isAuthenticated() && hasAuthority('ROLE_ADMIN')")
@Tag(name = "System Configuration Admin Management", description = "Operations related to system configuration")
public class SystemConfigAdminController {

    private final SystemConfigService systemConfigService;
    private final PagedResourcesAssembler<SystemConfigDto> pagedResourcesAssembler;
    private final SystemConfigDtoModelAdminAssembler systemConfigDtoAssembler;
    private final AdminPaginationService adminPaginationService;

    @Autowired
    public SystemConfigAdminController(SystemConfigService systemConfigService, PagedResourcesAssembler<SystemConfigDto> pagedResourcesAssembler, SystemConfigDtoModelAdminAssembler systemConfigDtoAssembler, AdminPaginationService adminPaginationService) {
        this.systemConfigService = systemConfigService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.systemConfigDtoAssembler = systemConfigDtoAssembler;
        this.adminPaginationService = adminPaginationService;
    }

    /**
     * Get all system configurations with pagination and sorting.
     *
     * @param page    the page number to retrieve
     * @param size    the number of items per page
     * @param sortBy  the field to sort by
     * @param request the HTTP request
     * @return a paginated list of system configurations
     */
    @GetMapping
    @Operation(summary = "Get all system configurations", description = "Returns a list of all system configurations", tags = {"System Configuration Admin Management"})
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<SystemConfigDto>>>> getAllSystemConfigs(
            @Parameter(name = "page", description = "Page number") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(name = "size", description = "Page size") @RequestParam(required = false) Integer size,
            @Parameter(name = "sort", description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            HttpServletRequest request
    ) {

        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, SystemConfig.class);

        Page<SystemConfigDto> systemConfigDtoList = systemConfigService.getAllSystemConfigs(pageable);
        PagedModel<EntityModel<SystemConfigDto>> pagedModel = pagedResourcesAssembler.toModel(
                systemConfigDtoList,
                dto -> systemConfigDtoAssembler.toModel(
                        dto,
                        adminPaginationService.getSafePage(page),
                        adminPaginationService.getSafeSize(size),
                        adminPaginationService.getSafeSortBy(sortBy, SystemConfig.class),
                        request)
        );

        ApiResponse<PagedModel<EntityModel<SystemConfigDto>>> response = ApiResponse.<PagedModel<EntityModel<SystemConfigDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("System configurations fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * Get a system configuration by its ID.
     *
     * @param systemConfigId the ID of the system configuration to retrieve
     * @param request        the HTTP request
     * @return the system configuration with the specified ID
     */
    @GetMapping("/{systemConfigId}")
    @Operation(summary = "Get a system configuration by ID", description = "Returns a system configuration by its ID")
    public ResponseEntity<ApiResponse<EntityModel<SystemConfigDto>>> getSystemConfigById(@Parameter(description = "ID of the system configuration") @RequestParam Long systemConfigId,
                                                                                         HttpServletRequest request) {
        SystemConfigDto systemConfigDto = systemConfigService.getSystemConfigById(systemConfigId);
        EntityModel<SystemConfigDto> entityModel = systemConfigDtoAssembler.toModel(systemConfigDto, request);
        ApiResponse<EntityModel<SystemConfigDto>> response = ApiResponse.<EntityModel<SystemConfigDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("System configuration fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Get a system configuration by its name.
     *
     * @param name    the name of the system configuration to retrieve
     * @param request the HTTP request
     * @return the system configuration with the specified name
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Get a system configuration by name", description = "Returns a system configuration by its name")
    public ResponseEntity<ApiResponse<EntityModel<SystemConfigDto>>> getSystemConfigByName(@Parameter(description = "Name of the system configuration") @RequestParam String name,
                                                                                           HttpServletRequest request) {
        SystemConfigDto systemConfigDto = systemConfigService.getSystemConfigByName(name);
        EntityModel<SystemConfigDto> entityModel = systemConfigDtoAssembler.toModel(systemConfigDto, request);
        ApiResponse<EntityModel<SystemConfigDto>> response = ApiResponse.<EntityModel<SystemConfigDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("System configuration fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Update a system configuration by its ID.
     *
     * @param systemConfigId        the ID of the system configuration to update
     * @param systemConfigUpdateDto the updated system configuration data
     * @param request               the HTTP request
     * @return the updated system configuration
     */
    @PutMapping("/{systemConfigId}")
    @Operation(summary = "Update a system configuration by ID", description = "Updates a system configuration by its ID")
    public ResponseEntity<ApiResponse<EntityModel<SystemConfigDto>>> updateSystemConfig(@Parameter(description = "ID of the system configuration") @RequestParam Long systemConfigId,
                                                                                        @Parameter(description = "Updated system configuration") @Valid @RequestBody SystemConfigUpdateDto systemConfigUpdateDto,
                                                                                        HttpServletRequest request) {
        SystemConfigDto updatedSystemConfig = systemConfigService.updateSystemConfig(systemConfigId, systemConfigUpdateDto);
        EntityModel<SystemConfigDto> entityModel = systemConfigDtoAssembler.toModel(updatedSystemConfig, request);
        ApiResponse<EntityModel<SystemConfigDto>> response = ApiResponse.<EntityModel<SystemConfigDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("System configuration updated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Create a new system configuration.
     *
     * @param systemConfigCreateDto the new system configuration data
     * @param request               the HTTP request
     * @return the created system configuration
     */
    @PostMapping
    @Operation(summary = "Create a new system configuration", description = "Creates a new system configuration")
    public ResponseEntity<ApiResponse<EntityModel<SystemConfigDto>>> addNewSystemConfig(@Parameter(description = "New system configuration") @Valid @RequestBody SystemConfigCreateDto systemConfigCreateDto,
                                                                                        HttpServletRequest request) {
        SystemConfigDto createdSystemConfig = systemConfigService.addNewSystemConfig(systemConfigCreateDto);
        EntityModel<SystemConfigDto> entityModel = systemConfigDtoAssembler.toModel(createdSystemConfig, request);
        ApiResponse<EntityModel<SystemConfigDto>> response = ApiResponse.<EntityModel<SystemConfigDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("System configuration created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Delete a system configuration by its ID.
     *
     * @param systemConfigId the ID of the system configuration to delete
     * @param request        the HTTP request
     * @return a response indicating the deletion was successful
     */
    @DeleteMapping("/{systemConfigId}")
    @Operation(summary = "Delete a system configuration by ID", description = "Deletes a system configuration by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteSystemConfig(@Parameter(description = "ID of the system configuration") @RequestParam Long systemConfigId,
                                                                HttpServletRequest request) {
        systemConfigService.deleteSystemConfig(systemConfigId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("System configuration deleted successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

}
