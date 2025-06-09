package com.muyategna.backend.location.dto.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityLocalizedDto {
    private Long id;
    private String name;
    private Long regionId;
    private String description;
}
