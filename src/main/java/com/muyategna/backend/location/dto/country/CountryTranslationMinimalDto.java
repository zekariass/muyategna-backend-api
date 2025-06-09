package com.muyategna.backend.location.dto.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryTranslationMinimalDto {
    private Long id;
    private String name;
    private Long countryId;
    private Long languageId;
}
