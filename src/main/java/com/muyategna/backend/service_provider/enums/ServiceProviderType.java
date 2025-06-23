package com.muyategna.backend.service_provider.enums;

public enum ServiceProviderType {
    FREELANCER("Freelancer"),
    SOLE_TRADER("Sole Trader"),
    LIMITED_COMPANY("Limited Company");

    private final String displayName;

    ServiceProviderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

