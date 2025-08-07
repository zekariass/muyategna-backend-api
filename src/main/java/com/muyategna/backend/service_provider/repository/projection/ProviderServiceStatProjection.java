package com.muyategna.backend.service_provider.repository.projection;

import java.math.BigDecimal;

public interface ProviderServiceStatProjection {
    Long getServiceProviderServiceId();

    Long getProviderId();

//    String getBusinessName();

//    String getBusinessDescription();

//    String getFullAddress();

//    BigDecimal getMaxTravelDistanceInKM();

//    String getBusinessLogoUrl();

//    BigDecimal getAverageRating();

//    Integer getNumberOfReviews();

//    Integer getYearsOfExperience();

//    String getBusinessEmail();

//    String getBusinessPhone();

    // Used for service provider list view by customers
    Double getDistanceMeters();

//    Long getInviteForQuoteId();

    Integer getTotalClaims();

    BigDecimal getAvgClaimTimeInMins();

    BigDecimal getAvgRating();

    Integer getCompletedLeads();

    Integer getTotalQuotes();

    Integer getAcceptedQuotes();

    Integer getDailyClaimCount();

    Integer getWeeklyClaimCount();

    Integer getTwoWeeklyClaimCount();

    Integer getMonthlyClaimCount();

    Integer getTotalNewJobNotifs();

    Integer getDailyNewJobNotifs();

    Integer getWeeklyNewJobNotifs();
}
