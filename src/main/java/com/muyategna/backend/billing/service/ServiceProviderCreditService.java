package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.ServiceProviderCredit;

public interface ServiceProviderCreditService {
    boolean hasServiceProviderEnoughCredit(Long serviceProviderId, Long serviceId);

    ServiceProviderCredit getServiceProviderCredit(Long serviceProviderId);

    void updateServiceProviderCredit(ServiceProviderCredit serviceProviderCredit);
}
