package com.muyategna.backend.service_provider.dto.service_provider_agreement;

import com.muyategna.backend.common.dto.legal_document.LegalDocumentLocalizedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderAgreementDto {
    private Long id;
    private LegalDocumentLocalizedDto legalDocument;
    private Long providerId;
    private Boolean isSigned = false;
    private LocalDateTime createdAt;
}