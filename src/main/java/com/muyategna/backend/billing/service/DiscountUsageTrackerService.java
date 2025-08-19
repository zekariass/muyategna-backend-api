package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.DiscountUsageTracker;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public interface DiscountUsageTrackerService {

    DiscountUsageTracker getDiscountUsageTracker(Long discountPlanId, UserEntityType userEntityType, Long userEntityId);

    void createOrUpdateDiscountUsageTracker(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider);
}
