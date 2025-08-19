package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Coupon;
import com.muyategna.backend.billing.entity.CouponUsageTracker;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.billing.repository.CouponRepository;
import com.muyategna.backend.billing.repository.CouponUsageTrackerRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class CouponUsageTrackerServiceImpl implements CouponUsageTrackerService {

    private final CouponUsageTrackerRepository repository;
    private final CouponRepository couponRepository;

    @Autowired
    public CouponUsageTrackerServiceImpl(CouponUsageTrackerRepository repository, CouponRepository couponRepository) {
        this.repository = repository;
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponUsageTracker getUserCouponUsageCount(Long couponId, UserEntityType couponUserType, Long couponUserId) {
        log.info("Retrieving coupon usage count for couponId: {}, userType: {}, userId: {}", couponId, couponUserType, couponUserId);
        return repository.findByCouponIdAndCouponUserTypeAndCouponUserId(couponId, couponUserType, couponUserId)
                .orElse(null);
    }

    @Override
    @Transactional
    public void createOrUpdateCouponUsageTracker(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        Coupon coupon = invoice.getCoupon();
        if (coupon == null) {
            return;
        }

        Long serviceProviderId = serviceProvider.getId();
        log.info("Processing Coupon Usage Tracker for Coupon ID: {}, Service Provider ID: {}", coupon.getId(), serviceProviderId);

        // Update or create user-specific usage tracker
        repository.findByCouponIdAndCouponUserTypeAndCouponUserId(
                        coupon.getId(), invoice.getPayerEntityType(), serviceProviderId)
                .ifPresentOrElse(tracker -> {
                    int newCount = tracker.getUseCount() + 1;
                    tracker.setUseCount(newCount);
                    tracker.setIsActive(hasRemainingUses(coupon, newCount));
                    repository.save(tracker);
                    log.info("Updated CouponUsageTracker: useCount={}, isActive={}", newCount, tracker.getIsActive());
                }, () -> {
                    CouponUsageTracker newTracker = new CouponUsageTracker();
                    newTracker.setCoupon(coupon);
                    newTracker.setCouponUserType(invoice.getPayerEntityType());
                    newTracker.setCouponUserId(serviceProviderId);

                    int initialCount = 1;
                    newTracker.setUseCount(initialCount);
                    newTracker.setUsageLimit(coupon.getPerUserLimit());
                    newTracker.setIsActive(hasRemainingUses(coupon, initialCount));
                    repository.save(newTracker);
                    log.info("Created new CouponUsageTracker: useCount={}, isActive={}", initialCount, newTracker.getIsActive());
                });

        // Safely increment coupon total use count using optimistic lock
        incrementCouponTotalUse(coupon);
    }

    private boolean hasRemainingUses(Coupon coupon, int useCount) {
        Integer perUserLimit = coupon.getPerUserLimit();
        return perUserLimit == null || useCount < perUserLimit;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementCouponTotalUse(Coupon coupon) {
        boolean success = false;
        int retryCount = 0;
        final int maxRetries = 3;

        while (!success && retryCount < maxRetries) {
            try {
                int updatedTotalUseCount = Objects.requireNonNullElse(coupon.getTotalUseCount(), 0) + 1;
                coupon.setTotalUseCount(updatedTotalUseCount);
                couponRepository.saveAndFlush(coupon); // flush triggers version check
                success = true;
                log.info("Coupon total use count updated to {}", updatedTotalUseCount);
            } catch (ObjectOptimisticLockingFailureException e) {
                retryCount++;
                log.warn("Optimistic lock on Coupon ID {}, retry attempt {}", coupon.getId(), retryCount);
                coupon = couponRepository.findById(coupon.getId())
                        .orElseThrow(() -> new IllegalStateException("Coupon not found during retry"));
            }
        }

        if (!success) {
            throw new IllegalStateException("Failed to update coupon total use count after " + maxRetries + " attempts due to concurrent updates");
        }
    }


}
