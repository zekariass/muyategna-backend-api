package com.muyategna.backend.location.dto.region;

import jakarta.validation.constraints.Min;
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
public class RegionCreateDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Country is required")
    @Min(value = 1, message = "Country ID must be greater than 0")
    private Long countryId;

    private String description;
}
