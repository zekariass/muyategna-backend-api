package com.muyategna.backend.service_provider.repository.projection;

import java.math.BigDecimal;

public interface ServiceProviderServiceWithDistanceAndStatisticsProjection {
    Long getServiceProviderServiceId();

    Long getServiceId();

    Long getProviderId();

    Boolean getIsActive();

    Double getDistanceMeters();

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
