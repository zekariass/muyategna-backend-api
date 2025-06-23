package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.dto.legal_document.LegalDocumentLocalizedDto;
import com.muyategna.backend.common.entity.LegalDocument;
import com.muyategna.backend.common.entity.LegalDocumentTranslation;
import com.muyategna.backend.common.mapper.LegalDocumentMapper;
import com.muyategna.backend.common.service.LegalDocumentService;
import com.muyategna.backend.common.service.LegalDocumentTranslationService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderAgreement;
import com.muyategna.backend.service_provider.mapper.ServiceProviderAgreementMapper;
import com.muyategna.backend.service_provider.repository.ServiceProviderAgreementRepository;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceProviderAgreementServiceImpl implements ServiceProviderAgreementService {

    private final ServiceProviderAgreementRepository serviceProviderAgreementRepository;
    private final LegalDocumentTranslationService legalDocumentTranslationService;
    private final LegalDocumentService legalDocumentService;
    private final ServiceProviderRepository serviceProviderRepository;

    @Autowired
    public ServiceProviderAgreementServiceImpl(ServiceProviderAgreementRepository serviceProviderAgreementRepository, LegalDocumentTranslationService legalDocumentTranslationService, LegalDocumentService legalDocumentService, ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderAgreementRepository = serviceProviderAgreementRepository;
        this.legalDocumentTranslationService = legalDocumentTranslationService;
        this.legalDocumentService = legalDocumentService;
        this.serviceProviderRepository = serviceProviderRepository;
    }

    @Override
    public List<ServiceProviderAgreement> createServiceProviderAgreements(List<ServiceProviderAgreement> providerAgreements) {
        log.info("Saving multiple provider agreements");
        if (providerAgreements == null || providerAgreements.isEmpty()) {
            log.warn("No agreements to save");
            return List.of();
        }
        List<ServiceProviderAgreement> savedProviderAgreements = serviceProviderAgreementRepository.saveAll(providerAgreements);
        log.info("Saved {} provider agreements", savedProviderAgreements.size());
        return savedProviderAgreements;
    }

    @Override
    public List<ServiceProviderAgreementDto> getServiceProviderAgreementsByServiceProviderId(Long serviceProviderId) {
        log.info("Getting provider agreements by service provider id: {}", serviceProviderId);
        List<ServiceProviderAgreement> providerAgreements = serviceProviderAgreementRepository.findByProviderId(serviceProviderId);
        if (providerAgreements == null || providerAgreements.isEmpty()) {
            return List.of();
        }

        return getLocalizedServiceProviderAgreementDtos(providerAgreements);
    }


    // Helper method
    @NotNull
    private List<ServiceProviderAgreementDto> getLocalizedServiceProviderAgreementDtos(List<ServiceProviderAgreement> providerAgreements) {
        // Get language from the context
        LanguageDto languageDto = LanguageContextHolder.getLanguage();

        List<Long> documentIds = providerAgreements.stream().map(agreement -> agreement.getDocument().getId()).toList();
        List<LegalDocumentTranslation> legalDocumentTranslations = legalDocumentTranslationService.findLegalDocumentTranslationsByDocumentIdInAndLanguageId(documentIds, languageDto.getId());

        Map<Long, LegalDocumentTranslation> legalDocumentTranslationsMap = legalDocumentTranslations.stream()
                .collect(Collectors.toMap(translation -> translation.getDocument().getId(), Function.identity()));

        List<ServiceProviderAgreementDto> providerAgreementsDto = new ArrayList<>();
        for (ServiceProviderAgreement providerAgreement : providerAgreements) {
            ServiceProviderAgreementDto providerAgreementDto = ServiceProviderAgreementMapper.toDto(providerAgreement);

            LegalDocumentLocalizedDto localizedDto = LegalDocumentMapper
                    .toLocalizedDto(providerAgreement.getDocument(),
                            legalDocumentTranslationsMap.get(providerAgreement.getDocument().getId()));

            providerAgreementDto.setLegalDocument(localizedDto);

            providerAgreementsDto.add(providerAgreementDto);
        }
        log.info("Retrieved {} provider agreements", providerAgreementsDto.size());
        return providerAgreementsDto;
    }


    @Override
    public ServiceProviderAgreementDto getServiceProviderAgreementByIdAndServiceProviderId(Long agreementId, Long serviceProviderId) {

        log.info("Retrieving provider agreement by id: {} and service provider id: {}", agreementId, serviceProviderId);
        ServiceProviderAgreement providerAgreement = serviceProviderAgreementRepository
                .findByIdAndProviderId(agreementId, serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider agreement not found with id: " + agreementId));

        ServiceProviderAgreementDto serviceProviderAgreementDto = getLocalizedServiceProviderAgreementDtos(List.of(providerAgreement)).getFirst();

        log.info("Retrieved provider agreement by id: {} and service provider id: {}", agreementId, serviceProviderId);
        return serviceProviderAgreementDto;
    }


    @Override
    public ServiceProviderAgreementDto signServiceProviderAgreement(Long serviceProviderId, ServiceProviderAgreementCreateDto serviceProviderAgreementDto) {
        log.info("Signing provider agreement for service provider id: {}", serviceProviderId + " with document ID: " + serviceProviderAgreementDto.getDocumentId());
        LegalDocument legalDocument = legalDocumentService.getLegalDocumentById(serviceProviderAgreementDto.getDocumentId());

        CountryDto countryDto = CountryContextHolder.getCountry();

        if (!legalDocument.getCountry().getId().equals(countryDto.getId())) {
            throw new ResourceNotFoundException("Legal document with ID: " + serviceProviderAgreementDto.getDocumentId() + " not found for country: " + countryDto.getName());
        }

        // Get service provider
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(
                () -> new ResourceNotFoundException("Service provider not found with ID: " + serviceProviderId));

        // Create provider agreement
        ServiceProviderAgreement serviceProviderAgreement = ServiceProviderAgreementMapper
                .toEntity(serviceProviderAgreementDto, legalDocument, serviceProvider);

        // Update provider agreement
        serviceProviderAgreement.setIsSigned(true);

        log.info("Signed provider agreement for service provider id: {}", serviceProviderId + " with document ID: " + serviceProviderAgreementDto.getDocumentId());
        return ServiceProviderAgreementMapper.toDto(serviceProviderAgreementRepository.save(serviceProviderAgreement));
    }
}
