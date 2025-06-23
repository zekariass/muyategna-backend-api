package com.muyategna.backend.common.service;

import com.muyategna.backend.common.entity.LegalDocumentTranslation;

import java.util.List;

public interface LegalDocumentTranslationService {
    List<LegalDocumentTranslation> findLegalDocumentTranslationsByDocumentIdInAndLanguageId(List<Long> documentIds, Long languageId);
}
