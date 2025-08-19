package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.ProductType;
import com.muyategna.backend.billing.repository.ServiceProviderWalletRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

        ServiceProviderWallet serviceProviderWallet = createNewWallet(savedServiceProvider);
        serviceProviderWalletRepository.save(serviceProviderWallet);
    }

    @NotNull
    private static ServiceProviderWallet createNewWallet(ServiceProvider savedServiceProvider) {
        ServiceProviderWallet serviceProviderWallet = new ServiceProviderWallet();
        serviceProviderWallet.setProvider(savedServiceProvider);
        serviceProviderWallet.setSubscription(null);
        serviceProviderWallet.setPaygoCredit(null);
        serviceProviderWallet.setTotalCredit(BigDecimal.ZERO);
        serviceProviderWallet.setAvailableCredit(BigDecimal.ZERO);
        serviceProviderWallet.setTotalHoldCredit(BigDecimal.ZERO);
        serviceProviderWallet.setCreatedAt(LocalDateTime.now());
        return serviceProviderWallet;
    }

    @Override
    public void createOrUpdateServiceProviderWallet(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        log.info("Creating or updating ServiceProviderWallet for ServiceProvider with id: {}", serviceProvider.getId());
        ServiceProviderWallet wallet = serviceProviderWalletRepository.findByProviderId(serviceProvider.getId())
                .orElse(createNewWallet(serviceProvider));

        // Get Product Plan
        Long productPlanId = invoice.getProductPlanId();
        ProductType productType = invoice.getProductType();
        if (productPlanId == null) {
            log.warn("Invoice {} has no productPlanId (subscriptionPlanId or addOnPlanId), skipping Service Provider Wallet creation or update.", invoice.getId());
            return;
        }

        // Get the credits included in the invoice
        BigDecimal creditsIncluded = invoice.getCreditsIncluded();

        wallet.setTotalCredit(wallet.getTotalCredit().add(creditsIncluded));
        wallet.setAvailableCredit(wallet.getAvailableCredit().add(creditsIncluded));

        serviceProviderWalletRepository.save(wallet);
    }
}
