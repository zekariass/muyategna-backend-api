package com.muyategna.backend.common.dto.legal_document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalDocumentUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Country is required")
    private Long countryId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Display name is required")
    private String displayName;

    private String version;
    private Boolean isRequired = true;
    private Boolean isActive = true;

    private LocalDateTime effectiveAt;
}
