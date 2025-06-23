package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.service_provider.dto.service_provider_verification.ServiceProviderVerificationCreatedDto;
import com.muyategna.backend.service_provider.dto.service_provider_verification.ServiceProviderVerificationDto;
import com.muyategna.backend.service_provider.dto.service_provider_verification.ServiceProviderVerificationUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderVerification;
import com.muyategna.backend.service_provider.entity.ServiceProviderVerificationType;
import com.muyategna.backend.user.entity.UserProfile;

public final class ServiceProviderVerificationMapper {

    public static ServiceProviderVerificationDto toDto(ServiceProviderVerification verification) {
        return ServiceProviderVerificationDto.builder()
                .id(verification.getId())
                .providerId(verification.getServiceProvider().getId())
                .typeId(verification.getVerificationType().getId())
                .verificationStatus(verification.getVerificationStatus())
                .documentUrl(verification.getDocumentUrl())
                .reasonForRejection(verification.getReasonForRejection())
                .verificationNote(verification.getVerificationNote())
                .verifiedById(verification.getVerifiedBy().getId())
                .createdAt(verification.getCreatedAt())
                .updatedAt(verification.getUpdatedAt())
                .build();
    }


    public static ServiceProviderVerification toEntity(ServiceProviderVerificationDto dto,
                                                       ServiceProvider serviceProvider,
                                                       ServiceProviderVerificationType verificationType,
                                                       UserProfile verifiedBy) {
        ServiceProviderVerification verification = new ServiceProviderVerification();
        verification.setId(dto.getId());
        verification.setServiceProvider(serviceProvider);
        verification.setVerificationType(verificationType);
        verification.setVerificationStatus(dto.getVerificationStatus());
        verification.setDocumentUrl(dto.getDocumentUrl());
        verification.setReasonForRejection(dto.getReasonForRejection());
        verification.setVerificationNote(dto.getVerificationNote());
        verification.setVerifiedBy(verifiedBy);
        verification.setCreatedAt(dto.getCreatedAt());
        return verification;
    }


    public static ServiceProviderVerification toEntity(ServiceProviderVerificationUpdateDto dto,
                                                       ServiceProvider serviceProvider,
                                                       ServiceProviderVerificationType verificationType,
                                                       UserProfile verifiedBy) {
        ServiceProviderVerification verification = new ServiceProviderVerification();
        verification.setId(dto.getId());
        verification.setServiceProvider(serviceProvider);
        verification.setVerificationType(verificationType);
        verification.setVerificationStatus(dto.getVerificationStatus());
        verification.setDocumentUrl(dto.getDocumentUrl());
        verification.setReasonForRejection(dto.getReasonForRejection());
        verification.setVerificationNote(dto.getVerificationNote());
        verification.setVerifiedBy(verifiedBy);
        return verification;
    }


    public static ServiceProviderVerification toEntity(ServiceProviderVerificationCreatedDto dto,
                                                       ServiceProvider serviceProvider,
                                                       ServiceProviderVerificationType verificationType,
                                                       UserProfile verifiedBy) {
        ServiceProviderVerification verification = new ServiceProviderVerification();
        verification.setServiceProvider(serviceProvider);
        verification.setVerificationType(verificationType);
        verification.setVerificationStatus(dto.getVerificationStatus());
        verification.setDocumentUrl(dto.getDocumentUrl());
        verification.setReasonForRejection(dto.getReasonForRejection());
        verification.setVerificationNote(dto.getVerificationNote());
        verification.setVerifiedBy(verifiedBy);
        return verification;
    }
}
