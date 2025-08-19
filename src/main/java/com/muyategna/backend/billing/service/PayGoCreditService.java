package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.PayGoCredit;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public interface PayGoCreditService {
    void save(PayGoCredit serviceProviderPayGoCredit);

    void createOrUpdatePayGoCredit(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider);
}
