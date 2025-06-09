package com.muyategna.backend.location.dto.sub_city_or_division;

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
public class SubCityOrDivisionUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "City is required")
    private Long cityId;

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
