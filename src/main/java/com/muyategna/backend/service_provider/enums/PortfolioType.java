package com.muyategna.backend.service_provider.enums;

public enum PortfolioType {
    WEBSITE("Website"),
    IMAGE("Image"),
    VIDEO("Video"),
    DOCUMENT("Document"),
    OTHER("Other");

    private final String displayName;

    PortfolioType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
