package com.muyategna.backend.common.dto.language;

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
public class LanguageCreateDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Locale is required")
    private String locale;

    @NotNull(message = "Country is required")
    private Long countryId;

    private String nativeName;
    private String direction;
    private String flagEmoji;

    private boolean isActive;
    private boolean isDefault;
    private boolean isGlobal;
}
