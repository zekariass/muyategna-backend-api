package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.entity.DiscountUsageTracker;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.billing.repository.DiscountPlanRepository;
import com.muyategna.backend.billing.repository.DiscountUsageTrackerRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class DiscountUsageTrackerServiceImpl implements DiscountUsageTrackerService {

    private final DiscountUsageTrackerRepository repository;
    private final DiscountPlanRepository planRepository;

    @Autowired
    public DiscountUsageTrackerServiceImpl(DiscountUsageTrackerRepository repository, DiscountPlanRepository planRepository) {
        this.repository = repository;
        this.planRepository = planRepository;
    }

    @Override
    public DiscountUsageTracker getDiscountUsageTracker(Long discountPlanId, UserEntityType discountUserType, Long discountUserId) {
        return repository.findByDiscountPlanIdAndDiscountUserTypeAndDiscountUserId(discountPlanId, discountUserType, discountUserId).orElse(null);
    }

    @Transactional
    @Override
    public void createOrUpdateDiscountUsageTracker(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        DiscountPlan discountPlan = invoice.getDiscountPlan();
        if (discountPlan == null) {
            return;
        }

        Long serviceProviderId = serviceProvider.getId();
        log.info("Processing Discount Usage Tracker for Discount Plan ID: {}, Service Provider ID: {}", discountPlan.getId(), serviceProviderId);

        // Update or create user-specific usage tracker
        repository.findByDiscountPlanIdAndDiscountUserTypeAndDiscountUserId(
                        discountPlan.getId(), invoice.getPayerEntityType(), serviceProviderId)
                .ifPresentOrElse(tracker -> {
                    int newCount = tracker.getUseCount() + 1;
                    tracker.setUseCount(newCount);
                    tracker.setIsActive(hasRemainingUses(discountPlan, newCount));
                    repository.save(tracker);
                    log.info("Updated DiscountUsageTracker: useCount={}, isActive={}", newCount, tracker.getIsActive());
                }, () -> {
                    DiscountUsageTracker newTracker = new DiscountUsageTracker();
                    newTracker.setDiscountPlan(discountPlan);
                    newTracker.setDiscountUserType(invoice.getPayerEntityType());
                    newTracker.setDiscountUserId(serviceProviderId);

                    int initialCount = 1;
                    newTracker.setUseCount(initialCount);
                    newTracker.setUsageLimit(discountPlan.getPerUserLimit());
                    newTracker.setIsActive(hasRemainingUses(discountPlan, initialCount));
                    repository.save(newTracker);
                    log.info("Created new DiscountUsageTracker: useCount={}, isActive={}", initialCount, newTracker.getIsActive());
                });

        // Safely increment coupon total use count using optimistic lock
        incrementCouponTotalUse(discountPlan);
    }

    private boolean hasRemainingUses(DiscountPlan discountPlan, int useCount) {
        Integer perUserLimit = discountPlan.getPerUserLimit();
        return perUserLimit == null || useCount < perUserLimit;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementCouponTotalUse(DiscountPlan discountPlan) {
        if (discountPlan == null) {
            log.warn("DiscountPlan is null, cannot increment total use count");
            return;
        }

        boolean success = false;
        int retryCount = 0;
        final int maxRetries = 3;

        while (!success && retryCount < maxRetries) {
            try {
                int updatedTotalUseCount = Objects.requireNonNullElse(discountPlan.getTotalUseCount(), 0) + 1;
                discountPlan.setTotalUseCount(updatedTotalUseCount);
                planRepository.saveAndFlush(discountPlan); // flush triggers version check
                success = true;
                log.info("DiscountPlan total use count updated to {}", updatedTotalUseCount);
            } catch (ObjectOptimisticLockingFailureException e) {
                retryCount++;
                log.warn("Optimistic lock on DiscountPlan ID {}, retry attempt {}", discountPlan.getId(), retryCount);
                discountPlan = planRepository.findById(discountPlan.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Coupon not found during retry"));
            }
        }

        if (!success) {
            throw new IllegalStateException("Failed to update coupon total use count after " + maxRetries + " attempts due to concurrent updates");
        }
    }
}
