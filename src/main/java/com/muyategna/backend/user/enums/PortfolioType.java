package com.muyategna.backend.user.enums;

import lombok.Getter;

@Getter
public enum PortfolioType {
    PDF("PDF File"),
    WEBSITE("Website"),
    LINKEDIN("LinkedIn Profile");

    private final String displayName;

    PortfolioType(String displayName) {
        this.displayName = displayName;
    }
}
