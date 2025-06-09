package com.muyategna.backend.location.dto.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryTranslationDto {
    private Long id;
    private Long countryId;
    private Long languageId;
    private String name;
    private String continent;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
