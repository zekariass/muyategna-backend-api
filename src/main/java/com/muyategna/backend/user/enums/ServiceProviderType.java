package com.muyategna.backend.user.enums;

import lombok.Getter;

@Getter
public enum ServiceProviderType {
    INDIVIDUAL("Individual"),
    COMPANY("Company");

    private final String displayName;

    ServiceProviderType(String displayName) {
        this.displayName = displayName;
    }
}
