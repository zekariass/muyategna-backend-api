package com.muyategna.backend.common.mapper;

import com.muyategna.backend.common.dto.legal_document.LegalDocumentTranslationCreateDto;
import com.muyategna.backend.common.dto.legal_document.LegalDocumentTranslationDto;
import com.muyategna.backend.common.dto.legal_document.LegalDocumentTranslationUpdateDto;
import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.entity.LegalDocument;
import com.muyategna.backend.common.entity.LegalDocumentTranslation;

import java.util.List;

public final class LegalDocumentTranslationMapper {

    public static LegalDocumentTranslationDto toDto(LegalDocumentTranslation legalDocumentTranslation) {
        if (legalDocumentTranslation == null) {
            return null;
        }
        return LegalDocumentTranslationDto.builder()
                .id(legalDocumentTranslation.getId())
                .documentId(legalDocumentTranslation.getDocument().getId())
                .languageId(legalDocumentTranslation.getLanguage().getId())
                .displayName(legalDocumentTranslation.getDocument().getName())
                .documentUrl(legalDocumentTranslation.getDocumentUrl())
                .content(legalDocumentTranslation.getContent())
                .createdAt(legalDocumentTranslation.getCreatedAt())
                .updatedAt(legalDocumentTranslation.getUpdatedAt())
                .build();
    }


    public static LegalDocumentTranslation toEntity(LegalDocumentTranslationDto legalDocumentTranslationDto,
                                                    LegalDocument legalDocument,
                                                    Language language) {
        if (legalDocumentTranslationDto == null) {
            return null;
        }
        LegalDocumentTranslation legalDocumentTranslation = new LegalDocumentTranslation();
        legalDocumentTranslation.setId(legalDocumentTranslationDto.getId());
        legalDocumentTranslation.setDisplayName(legalDocumentTranslationDto.getDisplayName());
        legalDocumentTranslation.setLanguage(language);
        legalDocumentTranslation.setDocument(legalDocument);
        legalDocumentTranslation.setDocumentUrl(legalDocumentTranslationDto.getDocumentUrl());
        legalDocumentTranslation.setContent(legalDocumentTranslationDto.getContent());
        legalDocumentTranslation.setCreatedAt(legalDocumentTranslationDto.getCreatedAt());
        return legalDocumentTranslation;
    }


    public static LegalDocumentTranslation toEntity(LegalDocumentTranslationUpdateDto legalDocumentTranslationDto,
                                                    LegalDocument legalDocument,
                                                    Language language) {
        if (legalDocumentTranslationDto == null) {
            return null;
        }
        LegalDocumentTranslation legalDocumentTranslation = new LegalDocumentTranslation();
        legalDocumentTranslation.setId(legalDocumentTranslationDto.getId());
        legalDocumentTranslation.setDisplayName(legalDocumentTranslationDto.getDisplayName());
        legalDocumentTranslation.setLanguage(language);
        legalDocumentTranslation.setDocument(legalDocument);
        legalDocumentTranslation.setDocumentUrl(legalDocumentTranslationDto.getDocumentUrl());
        legalDocumentTranslation.setContent(legalDocumentTranslationDto.getContent());
        return legalDocumentTranslation;
    }

    public static LegalDocumentTranslation toEntity(LegalDocumentTranslationCreateDto legalDocumentTranslationDto,
                                                    LegalDocument legalDocument,
                                                    Language language) {
        if (legalDocumentTranslationDto == null) {
            return null;
        }
        LegalDocumentTranslation legalDocumentTranslation = new LegalDocumentTranslation();
        legalDocumentTranslation.setDocument(legalDocument);
        legalDocumentTranslation.setDisplayName(legalDocumentTranslationDto.getDisplayName());
        legalDocumentTranslation.setLanguage(language);
        legalDocumentTranslation.setDocumentUrl(legalDocumentTranslationDto.getDocumentUrl());
        legalDocumentTranslation.setContent(legalDocumentTranslationDto.getContent());
        return legalDocumentTranslation;
    }


    public static List<LegalDocumentTranslationDto> toDtoList(List<LegalDocumentTranslation> legalDocumentTranslations) {
        return legalDocumentTranslations.stream().map(LegalDocumentTranslationMapper::toDto).toList();
    }
}
