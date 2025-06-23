package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.common.entity.LegalDocument;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderAgreement;

import java.util.List;

public final class ServiceProviderAgreementMapper {

    public static ServiceProviderAgreementDto toDto(ServiceProviderAgreement agreement) {
        return ServiceProviderAgreementDto.builder()
                .id(agreement.getId())
                .providerId(agreement.getProvider().getId())
                .isSigned(agreement.getIsSigned())
                .createdAt(agreement.getCreatedAt())
                .build();
    }


    public static ServiceProviderAgreement toEntity(ServiceProviderAgreementDto agreementDto,
                                                    LegalDocument document,
                                                    ServiceProvider serviceProvider) {
        if (agreementDto == null) {
            return null;
        }

        ServiceProviderAgreement agreement = new ServiceProviderAgreement();
        agreement.setId(agreementDto.getId());
        agreement.setDocument(document);
        agreement.setProvider(serviceProvider);
        agreement.setIsSigned(agreementDto.getIsSigned());
        agreement.setCreatedAt(agreementDto.getCreatedAt());
        return agreement;
    }


    public static ServiceProviderAgreement toEntity(ServiceProviderAgreementUpdateDto agreementDto,
                                                    LegalDocument document,
                                                    ServiceProvider serviceProvider) {
        if (agreementDto == null) {
            return null;
        }

        ServiceProviderAgreement agreement = new ServiceProviderAgreement();
        agreement.setId(agreementDto.getId());
        agreement.setDocument(document);
        agreement.setProvider(serviceProvider);
        agreement.setIsSigned(agreementDto.getIsSigned());
        return agreement;
    }


    public static ServiceProviderAgreement toEntity(ServiceProviderAgreementCreateDto agreementDto,
                                                    LegalDocument document,
                                                    ServiceProvider serviceProvider) {
        if (agreementDto == null) {
            return null;
        }

        ServiceProviderAgreement agreement = new ServiceProviderAgreement();
        agreement.setDocument(document);
        agreement.setProvider(serviceProvider);
        agreement.setIsSigned(agreementDto.getIsSigned());
        return agreement;
    }


    public static List<ServiceProviderAgreementDto> toDtoList(List<ServiceProviderAgreement> agreements) {
        return agreements.stream().map(ServiceProviderAgreementMapper::toDto).toList();
    }
}
