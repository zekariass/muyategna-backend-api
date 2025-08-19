package com.muyategna.backend.billing.enums;

public enum TransactionStatusEnum {
    INITIATED,
    AWAITING_BANK_TRANSFER,
    WEBHOOK_RECEIVED,
    COMPLETED,
    CANCELLED,
    FAILED,
    TIMEOUT,
    DISPUTED,
    REFUNDED
}
