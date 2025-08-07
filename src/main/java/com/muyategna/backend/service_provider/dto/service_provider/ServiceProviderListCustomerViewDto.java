package com.muyategna.backend.service_provider.dto.service_provider;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderListCustomerViewDto {

    private Long id;
    private String businessName;
    private String businessDescription;
    private String fullAddress;
    private BigDecimal maxTravelDistanceInKM;
    private String businessLogoUrl;
    private BigDecimal averageRating;
    private Integer numberOfReviews;
    private Integer yearsOfExperience;
    private String businessEmail;
    private String businessPhone;
    private Boolean isVerified;
}
