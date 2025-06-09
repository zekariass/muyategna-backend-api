package com.muyategna.backend.location.dto.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDto {
    private Long id;
    private String name;
    private Long countryId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
