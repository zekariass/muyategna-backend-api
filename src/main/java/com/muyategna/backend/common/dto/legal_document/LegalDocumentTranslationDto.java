package com.muyategna.backend.common.dto.legal_document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegalDocumentTranslationDto {
    private Long id;
    private Long documentId;
    private Long languageId;
    private String displayName;
    private String documentUrl;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
