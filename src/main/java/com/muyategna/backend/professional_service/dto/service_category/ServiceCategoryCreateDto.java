package com.muyategna.backend.professional_service.dto.service_category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCategoryCreateDto {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
