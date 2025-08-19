package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.AddOnPlan;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.PayGoCredit;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.mapper.AddOnPlanMapper;
import com.muyategna.backend.billing.repository.PayGoCreditRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PayGoCreditServiceImpl implements PayGoCreditService {
    private final PayGoCreditRepository payGoCreditRepository;
    private final AddOnPlanService addOnPlanService;

    public PayGoCreditServiceImpl(PayGoCreditRepository payGoCreditRepository, AddOnPlanService addOnPlanService) {
        this.payGoCreditRepository = payGoCreditRepository;
        this.addOnPlanService = addOnPlanService;
    }

    @Override
    public void save(PayGoCredit serviceProviderPayGoCredit) {
        log.info("Saving payGo credit with id: {}", serviceProviderPayGoCredit.getId());
        payGoCreditRepository.save(serviceProviderPayGoCredit);
    }

    @Override
    public void createOrUpdatePayGoCredit(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        Long productPlanId = invoice.getProductPlanId();
        if (productPlanId == null) {
            log.warn("Invoice {} has no productPlanId, skipping PayGo credit update.", invoice.getId());
            return;
        }

        AddOnPlan plan = AddOnPlanMapper.toEntity(addOnPlanService.getAddOnPlan(productPlanId));

        if (plan == null) {
            log.warn("No AddOnPlan found for productPlanId: {} (Invoice {})", productPlanId, invoice.getId());
            return;
        }

        BigDecimal creditsIncluded = plan.getCreditsIncluded() != null ? plan.getCreditsIncluded() : BigDecimal.ZERO;
        if (creditsIncluded.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("AddOnPlan {} includes no credits. Skipping PayGo credit update for Invoice {}", plan.getId(), invoice.getId());
            return;
        }

        payGoCreditRepository.findByProviderId(serviceProvider.getId())
                .ifPresentOrElse(existingCredit -> {
                    existingCredit.setBalance(existingCredit.getBalance().add(creditsIncluded));
                    save(existingCredit);
                    log.info("Updated PayGo credit for provider {} with {} credits. New balance: {}", serviceProvider.getId(), creditsIncluded, existingCredit.getBalance());
                }, () -> {
                    PayGoCredit newCredit = new PayGoCredit();
                    newCredit.setProvider(serviceProvider);
                    newCredit.setBalance(creditsIncluded);
                    newCredit.setAutoAddOnEnabled(false);
                    newCredit.setExtraData(null);
                    save(newCredit);
                    log.info("Created PayGo credit for provider {} with {} credits", serviceProvider.getId(), creditsIncluded);
                });
    }

}
