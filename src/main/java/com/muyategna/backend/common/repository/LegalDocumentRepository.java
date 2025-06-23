package com.muyategna.backend.common.repository;

import com.muyategna.backend.common.entity.LegalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegalDocumentRepository extends JpaRepository<LegalDocument, Long> {
}
