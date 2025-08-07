package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public interface ServiceProviderWalletService {
    ServiceProviderWallet findByServiceProviderIdWithRelatedEntities(Long serviceProviderId);

    void save(ServiceProviderWallet serviceProviderWallet);

    void createWalletForServiceProvider(ServiceProvider savedServiceProvider);
}
