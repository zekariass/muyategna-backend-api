package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoDto;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderTaxInfo;

public final class ServiceProviderTaxInfoMapper {

    public static ServiceProviderTaxInfoDto toDto(ServiceProviderTaxInfo taxInfo) {
        if (taxInfo == null) {
            return null;
        }

        return ServiceProviderTaxInfoDto.builder()
                .id(taxInfo.getId())
                .serviceProviderId(taxInfo.getServiceProvider().getId())
                .taxPayerIdNumber(taxInfo.getTaxPayerIdNumber())
                .isVatRegistered(taxInfo.getIsVatRegistered())
                .isTaxExempt(taxInfo.getIsTaxExempt())
                .taxExemptCertificateNumber(taxInfo.getTaxExemptCertificateNumber())
                .taxExemptionReason(taxInfo.getTaxExemptionReason())
                .incomeTaxClassification(taxInfo.getIncomeTaxClassification())
                .isActive(taxInfo.getIsActive())
                .createdAt(taxInfo.getCreatedAt())
                .updatedAt(taxInfo.getUpdatedAt())
                .build();
    }


    public static ServiceProviderTaxInfo toEntity(ServiceProviderTaxInfoDto taxInfoDto,
                                                  ServiceProvider serviceProvider) {
        if (taxInfoDto == null) {
            return null;
        }

        ServiceProviderTaxInfo taxInfo = new ServiceProviderTaxInfo();
        taxInfo.setId(taxInfoDto.getId());
        taxInfo.setServiceProvider(serviceProvider);
        taxInfo.setTaxPayerIdNumber(taxInfoDto.getTaxPayerIdNumber());
        taxInfo.setIsVatRegistered(taxInfoDto.getIsVatRegistered());
        taxInfo.setIsTaxExempt(taxInfoDto.getIsTaxExempt());
        taxInfo.setTaxExemptCertificateNumber(taxInfoDto.getTaxExemptCertificateNumber());
        taxInfo.setTaxExemptionReason(taxInfoDto.getTaxExemptionReason());
        taxInfo.setIncomeTaxClassification(taxInfoDto.getIncomeTaxClassification());
        taxInfo.setIsActive(taxInfoDto.getIsActive());
        taxInfo.setCreatedAt(taxInfoDto.getCreatedAt());
        return taxInfo;
    }


    public static ServiceProviderTaxInfo toEntity(ServiceProviderTaxInfoUpdateDto taxInfoDto,
                                                  ServiceProvider serviceProvider) {
        if (taxInfoDto == null) {
            return null;
        }

        ServiceProviderTaxInfo taxInfo = new ServiceProviderTaxInfo();
        taxInfo.setId(taxInfoDto.getId());
        taxInfo.setServiceProvider(serviceProvider);
        taxInfo.setTaxPayerIdNumber(taxInfoDto.getTaxPayerIdNumber());
        taxInfo.setIsVatRegistered(taxInfoDto.getIsVatRegistered());
        taxInfo.setIsTaxExempt(taxInfoDto.getIsTaxExempt());
        taxInfo.setTaxExemptCertificateNumber(taxInfoDto.getTaxExemptCertificateNumber());
        taxInfo.setTaxExemptionReason(taxInfoDto.getTaxExemptionReason());
        taxInfo.setIncomeTaxClassification(taxInfoDto.getIncomeTaxClassification());
        taxInfo.setIsActive(taxInfoDto.getIsActive());
        return taxInfo;
    }


    public static ServiceProviderTaxInfo toEntity(ServiceProviderTaxInfoCreateDto taxInfoDto,
                                                  ServiceProvider serviceProvider) {
        if (taxInfoDto == null) {
            return null;
        }

        ServiceProviderTaxInfo taxInfo = new ServiceProviderTaxInfo();
        taxInfo.setServiceProvider(serviceProvider);
        taxInfo.setTaxPayerIdNumber(taxInfoDto.getTaxPayerIdNumber());
        taxInfo.setIsVatRegistered(taxInfoDto.getIsVatRegistered());
        taxInfo.setIsTaxExempt(taxInfoDto.getIsTaxExempt());
        taxInfo.setTaxExemptCertificateNumber(taxInfoDto.getTaxExemptCertificateNumber());
        taxInfo.setTaxExemptionReason(taxInfoDto.getTaxExemptionReason());
        taxInfo.setIncomeTaxClassification(taxInfoDto.getIncomeTaxClassification());
        taxInfo.setIsActive(taxInfoDto.getIsActive());
        return taxInfo;
    }
}
