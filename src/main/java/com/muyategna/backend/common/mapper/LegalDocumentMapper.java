package com.muyategna.backend.common.mapper;

import com.muyategna.backend.common.dto.legal_document.LegalDocumentCreateDto;
import com.muyategna.backend.common.dto.legal_document.LegalDocumentDto;
import com.muyategna.backend.common.dto.legal_document.LegalDocumentLocalizedDto;
import com.muyategna.backend.common.dto.legal_document.LegalDocumentUpdateDto;
import com.muyategna.backend.common.entity.LegalDocument;
import com.muyategna.backend.common.entity.LegalDocumentTranslation;
import com.muyategna.backend.location.entity.Country;

import java.util.List;

public final class LegalDocumentMapper {
    public static LegalDocumentDto toDto(LegalDocument legalDocument) {
        if (legalDocument == null) {
            return null;
        }
        return LegalDocumentDto.builder()
                .id(legalDocument.getId())
                .countryId(legalDocument.getCountry().getId())
                .name(legalDocument.getName())
                .displayName(legalDocument.getDisplayName())
                .version(legalDocument.getVersion())
                .isRequired(legalDocument.getIsRequired())
                .isActive(legalDocument.getIsActive())
                .effectiveAt(legalDocument.getEffectiveAt())
                .createdAt(legalDocument.getCreatedAt())
                .updatedAt(legalDocument.getUpdatedAt())
                .build();
    }


    public static LegalDocument toEntity(LegalDocumentDto dto,
                                         Country country) {
        if (dto == null) {
            return null;
        }
        LegalDocument legalDocument = new LegalDocument();
        legalDocument.setId(dto.getId());
        legalDocument.setName(dto.getName());
        legalDocument.setDisplayName(dto.getDisplayName());
        legalDocument.setVersion(dto.getVersion());
        legalDocument.setIsRequired(dto.getIsRequired());
        legalDocument.setIsActive(dto.getIsActive());
        legalDocument.setEffectiveAt(dto.getEffectiveAt());
        legalDocument.setCountry(country);
        legalDocument.setCreatedAt(dto.getCreatedAt());
        legalDocument.setUpdatedAt(dto.getUpdatedAt());
        return legalDocument;
    }


    public static LegalDocument toEntity(LegalDocumentUpdateDto dto,
                                         Country country) {
        if (dto == null) {
            return null;
        }
        LegalDocument legalDocument = new LegalDocument();
        legalDocument.setId(dto.getId());
        legalDocument.setName(dto.getName());
        legalDocument.setDisplayName(dto.getDisplayName());
        legalDocument.setVersion(dto.getVersion());
        legalDocument.setIsRequired(dto.getIsRequired());
        legalDocument.setIsActive(dto.getIsActive());
        legalDocument.setEffectiveAt(dto.getEffectiveAt());
        legalDocument.setCountry(country);
        return legalDocument;
    }


    public static LegalDocument toEntity(LegalDocumentCreateDto dto,
                                         Country country) {
        if (dto == null) {
            return null;
        }
        LegalDocument legalDocument = new LegalDocument();
        legalDocument.setName(dto.getName());
        legalDocument.setDisplayName(dto.getDisplayName());
        legalDocument.setVersion(dto.getVersion());
        legalDocument.setIsRequired(dto.getIsRequired());
        legalDocument.setIsActive(dto.getIsActive());
        legalDocument.setEffectiveAt(dto.getEffectiveAt());
        legalDocument.setCountry(country);
        return legalDocument;
    }


    public static LegalDocumentLocalizedDto toLocalizedDto(LegalDocument legalDocument,
                                                           LegalDocumentTranslation translation) {
        if (legalDocument == null) {
            return null;
        }

        if (translation == null) {
            return null;
        }

        return LegalDocumentLocalizedDto.builder()
                .id(legalDocument.getId())
                .displayName(translation.getDisplayName())
                .documentUrl(translation.getDocumentUrl())
                .content(translation.getContent())
                .languageName(translation.getLanguage().getName())
                .version(legalDocument.getVersion())
                .build();
    }


    public static List<LegalDocumentDto> toDtoList(List<LegalDocument> legalDocuments) {
        return legalDocuments.stream().map(LegalDocumentMapper::toDto).toList();
    }
}
