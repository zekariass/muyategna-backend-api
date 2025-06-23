package com.muyategna.backend.service_provider.dto.service_provider;


import com.muyategna.backend.location.dto.address.AddressCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoCreateDto;
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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderCreateDto {

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business description is required")
    @Size(min = 30, message = "Business description must be at least 30 characters long")
    private String businessDescription;

    @NotNull(message = "Service Provider Type is required")
    private ServiceProviderType serviceProviderType;

    private List<ServiceProviderServiceCreateDto> subscribedServices;

    @NotNull(message = "Business address is required")
    private AddressCreateDto businessAddressCreateDto;

    @NotNull(message = "Number of employees is required")
    @Min(value = 1, message = "Number of employees must be at least 1")
    private Integer numEmployees = 1;

    private BigDecimal maxTravelDistanceInKM;
    private String portfolioUrl;
    private PortfolioType portfolioType;
    private String businessLogoUrl;
    private Integer yearsOfExperience;


    @NotNull(message = "Service Provider Tax Info is required")
    private ServiceProviderTaxInfoCreateDto serviceProviderTaxInfoCreateDto;

    private List<ServiceProviderAgreementCreateDto> serviceProviderAgreementCreateDto;

}
