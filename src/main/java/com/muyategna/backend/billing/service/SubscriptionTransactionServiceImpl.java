package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.*;
import com.muyategna.backend.billing.mapper.SubscriptionPlanMapper;
import com.muyategna.backend.billing.repository.SubscriptionTransactionRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionTransactionServiceImpl implements SubscriptionTransactionService {

    private final SubscriptionTransactionRepository subscriptionTransactionRepository;
    private final SubscriptionPlanService subscriptionPlanService;

    @Autowired
    public SubscriptionTransactionServiceImpl(SubscriptionTransactionRepository subscriptionTransactionRepository, SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionTransactionRepository = subscriptionTransactionRepository;
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @Override
    public void createSubscriptionTransaction(ServiceProviderSubscription subscription, Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        log.info("Creating Subscription Transaction for Service Provider with ID: {}", serviceProvider.getId());

        // Get the subscriptionPlan

        // Create a new SubscriptionTransaction entity
        SubscriptionTransaction subscriptionTransaction = new SubscriptionTransaction();
        subscriptionTransaction.setProvider(serviceProvider);
        subscriptionTransaction.setTransaction(updatedTransaction);

        // Get SubscriptionPlan from DB
        SubscriptionPlan subscriptionPlan = SubscriptionPlanMapper.toEntity(subscriptionPlanService.getSubscriptionPlan(invoice.getProductPlanId()));
        subscriptionTransaction.setSubscriptionPlan(subscriptionPlan);

        subscriptionTransaction.setCreditsIncluded(subscriptionPlan.getCreditsIncluded());
        subscriptionTransaction.setExtraData(null);
        subscriptionTransaction.setServiceProviderSubscription(subscription);

        // Save the SubscriptionTransaction
        subscriptionTransactionRepository.save(subscriptionTransaction);

        log.info("Subscription Transaction created successfully for Service Provider with ID: {}", serviceProvider.getId());
    }
}
