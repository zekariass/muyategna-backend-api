package com.muyategna.backend.service_provider.dto.service_provider;


import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceLocalizedDto;
import com.muyategna.backend.service_provider.enums.PortfolioType;
import com.muyategna.backend.service_provider.enums.ServiceProviderType;
import com.muyategna.backend.service_provider.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderDetailDto {

    private Long id;
    private String businessName;
    private String businessDescription;
    private ServiceProviderType serviceProviderType;
    private String fullAddress;
    private Integer numEmployees;
    private BigDecimal maxTravelDistanceInKM;
    private String portfolioUrl;
    private PortfolioType portfolioType;
    private String businessLogoUrl;
    private VerificationStatus verificationStatus;
    private BigDecimal averageRating;
    private Integer numberOfReviews;
    private Integer yearsOfExperience;
    private Boolean isVerified;
    private String businessEmail;
    private String businessPhone;
    private String tiktok;
    private String whatsApp;
    private String facebook;
    private String linkedin;
    private String instagram;
    private String website;
    private Long defaultLanguageId;

    private List<ServiceProviderServiceLocalizedDto> subscribedServices;


}
