package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;

public interface InvoiceService {
    Invoice createInvoice(PaymentRequirement paymentRequirement);

    void saveInvoice(Invoice invoice);
}
