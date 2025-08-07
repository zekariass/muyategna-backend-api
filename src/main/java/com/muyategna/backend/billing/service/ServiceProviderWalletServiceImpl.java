package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.repository.ServiceProviderWalletRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class ServiceProviderWalletServiceImpl implements ServiceProviderWalletService {

    private final ServiceProviderWalletRepository serviceProviderWalletRepository;

    @Autowired
    public ServiceProviderWalletServiceImpl(ServiceProviderWalletRepository serviceProviderWalletRepository) {
        this.serviceProviderWalletRepository = serviceProviderWalletRepository;
    }

    @Override
    public ServiceProviderWallet findByServiceProviderIdWithRelatedEntities(Long serviceProviderId) {
        log.info("Fetching ServiceProviderWallet by serviceProviderId: {}", serviceProviderId);
        return serviceProviderWalletRepository.findByServiceProviderIdWithRelatedEntities(serviceProviderId).orElseThrow(() -> new ResourceNotFoundException("ServiceProviderWallet not found"));
    }

    @Override
    public void save(ServiceProviderWallet serviceProviderWallet) {
        log.info("Saving ServiceProviderWallet with id: {}", serviceProviderWallet.getId());
        serviceProviderWalletRepository.save(serviceProviderWallet);
    }

    @Transactional
    @Override
    public void createWalletForServiceProvider(ServiceProvider savedServiceProvider) {
        log.info("Creating ServiceProviderWallet for ServiceProvider with id: {}", savedServiceProvider.getId());

        ServiceProviderWallet serviceProviderWallet = new ServiceProviderWallet();
        serviceProviderWallet.setProvider(savedServiceProvider);
        serviceProviderWallet.setSubscription(null);
        serviceProviderWallet.setPaygoCredit(null);
        serviceProviderWallet.setTotalCredit(BigDecimal.ZERO);
        serviceProviderWallet.setAvailableCredit(BigDecimal.ZERO);
        serviceProviderWallet.setTotalHoldCredit(BigDecimal.ZERO);
        serviceProviderWalletRepository.save(serviceProviderWallet);
    }
}
