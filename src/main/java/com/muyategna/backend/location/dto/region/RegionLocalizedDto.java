package com.muyategna.backend.location.dto.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionLocalizedDto {
    private Long id;
    private String name;
    private Long countryId;
    private String description;

}
