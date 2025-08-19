package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public interface AddOnTransactionService {
    void createAddOnTransaction(Invoice invoice, Transaction transaction, ServiceProvider serviceProvider);
}
