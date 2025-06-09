package com.muyategna.backend.user.enums;

import lombok.Getter;

@Getter
public enum VerificationStatus {
    PENDING("Pending"),
    VERIFIED("Verified"),
    REJECTED("Rejected");

    private final String displayName;

    VerificationStatus(String displayName) {
        this.displayName = displayName;
    }
}