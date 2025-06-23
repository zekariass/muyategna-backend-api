package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementDto;
import com.muyategna.backend.service_provider.entity.ServiceProviderAgreement;

import java.util.List;

public interface ServiceProviderAgreementService {
    List<ServiceProviderAgreement> createServiceProviderAgreements(List<ServiceProviderAgreement> providerAgreements);

    List<ServiceProviderAgreementDto> getServiceProviderAgreementsByServiceProviderId(Long serviceProviderId);

    ServiceProviderAgreementDto getServiceProviderAgreementByIdAndServiceProviderId(Long agreementId, Long serviceProviderId);

    ServiceProviderAgreementDto signServiceProviderAgreement(Long serviceProviderId, ServiceProviderAgreementCreateDto serviceProviderAgreementDto);
}
