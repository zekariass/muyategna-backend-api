package com.muyategna.backend.common.repository;

import com.muyategna.backend.common.entity.LegalDocumentTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LegalDocumentTranslationRepository extends JpaRepository<LegalDocumentTranslation, Long> {
    List<LegalDocumentTranslation> findByLanguage_IdAndDocument_IdIn(Long languageId, List<Long> documentIds);
}
