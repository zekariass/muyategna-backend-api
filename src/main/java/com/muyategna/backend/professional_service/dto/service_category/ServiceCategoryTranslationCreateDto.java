package com.muyategna.backend.professional_service.dto.service_category;

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
public class ServiceCategoryTranslationCreateDto {
    @NotNull(message = "Service category is required")
    private Long serviceCategory;

    @NotNull(message = "Created at is required")
    private Long language;

    @NotBlank(message = "Name is required")
    private String name;
}
