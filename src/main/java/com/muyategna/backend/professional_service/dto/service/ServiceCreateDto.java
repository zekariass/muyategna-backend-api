package com.muyategna.backend.professional_service.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCreateDto {
    @NotNull(message = "Service category ID cannot be null")
    private Long serviceCategoryId;

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
