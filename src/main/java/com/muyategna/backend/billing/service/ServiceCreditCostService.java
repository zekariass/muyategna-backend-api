package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;

public interface ServiceCreditCostService {
    ServiceCreditCostDto findByServiceId(Long serviceId);
}
