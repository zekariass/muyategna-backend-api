package com.muyategna.backend.location.dto.region;

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
public class RegionTranslationUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Region is required")
    private Long regionId;

    @NotNull(message = "Language is required")
    private Long languageId;

    @NotBlank(message = "Name is required")
    private String name;
    private String description;

}
