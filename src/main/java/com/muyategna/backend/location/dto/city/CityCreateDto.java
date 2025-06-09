package com.muyategna.backend.location.dto.city;

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
public class CityCreateDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Region is required")
    private Long regionId;

    private String description;
}
