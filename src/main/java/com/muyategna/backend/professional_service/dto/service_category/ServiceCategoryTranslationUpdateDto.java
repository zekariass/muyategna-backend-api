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
public class ServiceCategoryTranslationUpdateDto {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Service Category cannot be null")
    private Long serviceCategory;

    @NotNull(message = "Language cannot be null")
    private Long language;

    @NotBlank(message = "Name is required")
    private String name;
}
