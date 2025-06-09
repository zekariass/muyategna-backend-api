package com.muyategna.backend.location.dto.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDto {
    private Long id;
    private String name;
    private Long regionId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
