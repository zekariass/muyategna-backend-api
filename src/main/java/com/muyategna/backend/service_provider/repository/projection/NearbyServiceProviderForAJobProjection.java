package com.muyategna.backend.service_provider.repository.projection;

import java.math.BigDecimal;

/**
 * A projection interface that is used to display nearby SPs for the customer
 *
 * @author Firstname Lastname
 */
public interface NearbyServiceProviderForAJobProjection {
    Long getServiceProviderId();

    String getBusinessName();

    String getBusinessDescription();

    String getFullAddress();

    BigDecimal getMaxTravelDistanceInKM();

    String getBusinessLogoUrl();

    BigDecimal getAverageRating();

    Integer getNumberOfReviews();

    Integer getYearsOfExperience();

    String getBusinessEmail();

    String getBusinessPhone();

    // If service provider is invited for quote, then this field will be set
    Long getInviteForQuoteId();

    // If the service provider already claimed the job, then this field will be set
    Long getClaimId();

    Double getDistanceFromJobInMeters();
}


