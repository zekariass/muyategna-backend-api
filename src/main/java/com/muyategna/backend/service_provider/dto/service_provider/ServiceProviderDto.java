package com.muyategna.backend.service_provider.dto.service_provider;


import com.muyategna.backend.service_provider.enums.PortfolioType;
import com.muyategna.backend.service_provider.enums.ServiceProviderType;
import com.muyategna.backend.service_provider.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderDto {

    private Long id;
    private String businessName;
    private String businessDescription;
    private ServiceProviderType serviceProviderType;
    private Long businessAddressId;
    private Integer numEmployees;
    private BigDecimal maxTravelDistanceInKM;
    private String portfolioUrl;
    private PortfolioType portfolioType;
    private String businessLogoUrl;
    private VerificationStatus verificationStatus;
    private BigDecimal averageRating;
    private Integer numberOfReviews;
    private Integer yearsOfExperience;
    private Long taxInfoId;
    private Boolean isActive;
    private Boolean isVerified;
    private Boolean isBlocked;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

}
