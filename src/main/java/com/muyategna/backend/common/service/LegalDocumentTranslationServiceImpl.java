package com.muyategna.backend.common.service;

import com.muyategna.backend.common.entity.LegalDocumentTranslation;
import com.muyategna.backend.common.repository.LegalDocumentTranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LegalDocumentTranslationServiceImpl implements LegalDocumentTranslationService {

    private final LegalDocumentTranslationRepository legalDocumentTranslationRepository;

    @Autowired
    public LegalDocumentTranslationServiceImpl(LegalDocumentTranslationRepository legalDocumentTranslationRepository) {
        this.legalDocumentTranslationRepository = legalDocumentTranslationRepository;
    }

    @Override
    public List<LegalDocumentTranslation> findLegalDocumentTranslationsByDocumentIdInAndLanguageId(List<Long> documentIds, Long languageId) {
        log.info("Getting legal document translations by document ids: {}", documentIds + " and language id: " + languageId);
        List<LegalDocumentTranslation> legalDocumentTranslations = legalDocumentTranslationRepository.findByLanguage_IdAndDocument_IdIn(languageId, documentIds);
        log.info("Retrieved legal document translations of size: {}", legalDocumentTranslations.size());
        return legalDocumentTranslations;
    }
}
