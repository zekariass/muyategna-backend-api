package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public interface SubscriptionTransactionService {
    void createSubscriptionTransaction(ServiceProviderSubscription subscription, Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider);
}
