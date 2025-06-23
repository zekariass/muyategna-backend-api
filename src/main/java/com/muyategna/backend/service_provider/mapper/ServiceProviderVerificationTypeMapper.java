package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.service_provider.dto.verification_type.ServiceProviderVerificationTypeCreateDto;
import com.muyategna.backend.service_provider.dto.verification_type.ServiceProviderVerificationTypeDto;
import com.muyategna.backend.service_provider.dto.verification_type.ServiceProviderVerificationTypeUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProviderVerificationType;

public final class ServiceProviderVerificationTypeMapper {

    public static ServiceProviderVerificationTypeDto toDto(ServiceProviderVerificationType verificationType) {
        if (verificationType == null) {
            return null;
        }

        return ServiceProviderVerificationTypeDto.builder()
                .id(verificationType.getId())
                .name(verificationType.getName())
                .providerType(verificationType.getProviderType())
                .isMandatory(verificationType.getIsMandatory())
                .documentRequired(verificationType.getDocumentRequired())
                .createdAt(verificationType.getCreatedAt())
                .updatedAt(verificationType.getUpdatedAt())
                .build();
    }


    public static ServiceProviderVerificationType toEntity(ServiceProviderVerificationTypeDto verificationTypeDto) {
        if (verificationTypeDto == null) {
            return null;
        }

        ServiceProviderVerificationType verificationType = new ServiceProviderVerificationType();
        verificationType.setId(verificationTypeDto.getId());
        verificationType.setName(verificationTypeDto.getName());
        verificationType.setProviderType(verificationTypeDto.getProviderType());
        verificationType.setIsMandatory(verificationTypeDto.getIsMandatory());
        verificationType.setDocumentRequired(verificationTypeDto.getDocumentRequired());
        verificationType.setCreatedAt(verificationTypeDto.getCreatedAt());
        verificationType.setUpdatedAt(verificationTypeDto.getUpdatedAt());

        return verificationType;
    }


    public static ServiceProviderVerificationType toEntity(ServiceProviderVerificationTypeCreateDto verificationTypeDto) {
        if (verificationTypeDto == null) {
            return null;
        }

        ServiceProviderVerificationType verificationType = new ServiceProviderVerificationType();
        verificationType.setName(verificationTypeDto.getName());
        verificationType.setProviderType(verificationTypeDto.getProviderType());
        verificationType.setIsMandatory(verificationTypeDto.getIsMandatory());
        verificationType.setDocumentRequired(verificationTypeDto.getDocumentRequired());

        return verificationType;
    }


    public static ServiceProviderVerificationType toEntity(ServiceProviderVerificationTypeUpdateDto verificationTypeDto) {
        if (verificationTypeDto == null) {
            return null;
        }

        ServiceProviderVerificationType verificationType = new ServiceProviderVerificationType();
        verificationType.setId(verificationTypeDto.getId());
        verificationType.setName(verificationTypeDto.getName());
        verificationType.setProviderType(verificationTypeDto.getProviderType());
        verificationType.setIsMandatory(verificationTypeDto.getIsMandatory());
        verificationType.setDocumentRequired(verificationTypeDto.getDocumentRequired());

        return verificationType;
    }
}
