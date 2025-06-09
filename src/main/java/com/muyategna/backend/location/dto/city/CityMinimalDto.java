package com.muyategna.backend.location.dto.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityMinimalDto {
    private Long id;
    private Long regionId;
    private String name;
}
