package com.muyategna.backend.location.dto.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionMinimalDto {
    private Long id;
    private Long countryId;
    private String name;
}
