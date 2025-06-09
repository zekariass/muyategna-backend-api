package com.muyategna.backend.request_and_lead.enums;

public enum StartDateRange {
    URGENT("Urgent"),
    WITHIN_2_DAYS("Within 2 days"),
    WITHIN_A_WEEK("Within a week"),
    WITHIN_2_WEEKS("Within 2 weeks"),
    WITHIN_A_MONTH("Within a month"),
    WITHIN_3_MONTHS("Within 3 months"),
    FLEXIBLE("Flexible");

    private final String displayName;

    StartDateRange(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
