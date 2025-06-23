package com.muyategna.backend.common.dto.legal_document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalDocumentTranslationCreateDto {
    @NotNull(message = "Document ID is required")
    private Long documentId;

    @NotNull(message = "Language ID is required")
    private Long languageId;

    @NotBlank(message = "Display Name is required")
    private String displayName;

    private String documentUrl;
    private String content;
}