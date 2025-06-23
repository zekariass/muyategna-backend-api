package com.muyategna.backend.service_provider.dto.service_provider;


import com.muyategna.backend.service_provider.enums.PortfolioType;
import com.muyategna.backend.service_provider.enums.ServiceProviderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderUpdateDto {

    @NotNull(message = "Service provider ID is required")
    private Long id;

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business description is required")
    @Size(min = 30, message = "Business description must be at least 30 characters long")
    private String businessDescription;

    private ServiceProviderType serviceProviderType;

    @NotNull(message = "Number of employees is required")
    @Min(value = 1, message = "Number of employees must be at least 1")
    private Integer numEmployees;

    private BigDecimal maxTravelDistanceInKM;
    private String portfolioUrl;
    private PortfolioType portfolioType;
    private String businessLogoUrl;
    private Integer yearsOfExperience;
    private Boolean isActive;

}
