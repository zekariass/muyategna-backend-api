package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.entity.ServiceCreditCost;

public final class ServiceCreditCostMapper {
    public static ServiceCreditCostDto toDto(ServiceCreditCost serviceCreditCost) {
        if (serviceCreditCost == null) {
            return null;
        }
        return ServiceCreditCostDto.builder()
                .id(serviceCreditCost.getId())
                .serviceId(serviceCreditCost.getService().getId())
                .paygCreditsPerLead(serviceCreditCost.getPaygCreditsPerLead())
                .subsCreditsPerLead(serviceCreditCost.getSubsCreditsPerLead())
                .createdAt(serviceCreditCost.getCreatedAt())
                .updatedAt(serviceCreditCost.getUpdatedAt())
                .build();
    }


    public static ServiceCreditCost toEntity(ServiceCreditCostDto serviceCreditCostDto) {
        if (serviceCreditCostDto == null) {
            return null;
        }
        ServiceCreditCost serviceCreditCost = new ServiceCreditCost();
        serviceCreditCost.setId(serviceCreditCostDto.getId());
        serviceCreditCost.setPaygCreditsPerLead(serviceCreditCostDto.getPaygCreditsPerLead());
        serviceCreditCost.setSubsCreditsPerLead(serviceCreditCostDto.getSubsCreditsPerLead());
        serviceCreditCost.setCreatedAt(serviceCreditCostDto.getCreatedAt());
        serviceCreditCost.setUpdatedAt(serviceCreditCostDto.getUpdatedAt());
        return serviceCreditCost;
    }
}
