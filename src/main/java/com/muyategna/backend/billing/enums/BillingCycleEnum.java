package com.muyategna.backend.billing.enums;

public enum BillingCycleEnum {
    DAY(1),
    WEEK(7),
    MONTH(30),
    YEAR(365);

    private final int days;

    BillingCycleEnum(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
