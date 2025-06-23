package com.muyategna.backend.common.service;

import com.muyategna.backend.common.entity.LegalDocument;
import com.muyategna.backend.common.repository.LegalDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LegalDocumentServiceImpl implements LegalDocumentService {

    private final LegalDocumentRepository legalDocumentRepository;

    @Autowired
    public LegalDocumentServiceImpl(LegalDocumentRepository legalDocumentRepository) {
        this.legalDocumentRepository = legalDocumentRepository;
    }

    @Override
    public LegalDocument getLegalDocumentById(Long documentId) {
        log.info("Retrieving legal document by id: {}", documentId);
        LegalDocument legalDocument = legalDocumentRepository.findById(documentId).orElseThrow(() -> new RuntimeException("Legal document not found with id: " + documentId));
        log.info("Retrieved legal document: {}", legalDocument);
        return legalDocument;
    }
}
