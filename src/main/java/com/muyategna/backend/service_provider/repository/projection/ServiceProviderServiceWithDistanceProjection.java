package com.muyategna.backend.service_provider.repository.projection;

import java.math.BigDecimal;

public interface ServiceProviderServiceWithDistanceProjection {
    Long getId();
    Long getServiceId();
    Long getProviderId();
    Boolean getIsActive();
    Double getDistanceMeters();

    // Add more fields as needed
    BigDecimal getCreditBalance();
    BigDecimal getCreditsPerLead();
    Boolean getHasEnoughCredits();
    Boolean getHasActiveSubscription();
}
