package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.CouponUsageTracker;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public interface CouponUsageTrackerService {
    CouponUsageTracker getUserCouponUsageCount(Long couponId, UserEntityType couponUserType, Long couponUserId);

    void createOrUpdateCouponUsageTracker(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider);
}
