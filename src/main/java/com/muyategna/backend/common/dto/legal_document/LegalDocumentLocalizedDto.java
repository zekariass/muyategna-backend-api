package com.muyategna.backend.common.dto.legal_document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Translated legal document DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegalDocumentLocalizedDto {
    private Long id;
    private String displayName;
    private String documentUrl;
    private String content;
    private String languageName;
    private String version;
}
