package com.muyategna.backend.common.dto.legal_document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalDocumentDto {
    private Long id;
    private Long countryId;
    private String name;
    private String displayName;
    private String version;
    private Boolean isRequired;
    private Boolean isActive;
    private LocalDateTime effectiveAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
