package com.muyategna.backend.location.dto.sub_city_or_division;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCityOrDivisionLocalizedDto {
    private Long id;
    private Long cityId;
    private String name;
    private String description;
}
