package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public final class ServiceProviderMapper {
    public static ServiceProviderDto toDto(ServiceProvider serviceProvider) {
        if (serviceProvider == null) {
            return null;
        }

        return ServiceProviderDto.builder()
                .id(serviceProvider.getId())
                .businessName(serviceProvider.getBusinessName())
                .businessDescription(serviceProvider.getBusinessDescription())
                .serviceProviderType(serviceProvider.getServiceProviderType())
                .businessAddressId(serviceProvider.getBusinessAddress().getId())
                .numEmployees(serviceProvider.getNumEmployees())
                .maxTravelDistanceInKM(serviceProvider.getMaxTravelDistanceInKM())
                .portfolioType(serviceProvider.getPortfolioType())
                .businessLogoUrl(serviceProvider.getBusinessLogoUrl())
                .averageRating(serviceProvider.getAverageRating())
                .numberOfReviews(serviceProvider.getNumberOfReviews())
                .yearsOfExperience(serviceProvider.getYearsOfExperience())
                .verificationStatus(serviceProvider.getVerificationStatus())
                .portfolioUrl(serviceProvider.getPortfolioUrl())
                .isActive(serviceProvider.getIsActive())
                .isVerified(serviceProvider.getIsVerified())
                .isBlocked(serviceProvider.getIsBlocked())
                .registeredAt(serviceProvider.getRegisteredAt())
                .build();
    }


    public static ServiceProvider toEntity(ServiceProviderDto serviceProviderDto, Address businessAddress) {
        if (serviceProviderDto == null) {
            return null;
        }

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(serviceProviderDto.getId());
        serviceProvider.setBusinessName(serviceProviderDto.getBusinessName());
        serviceProvider.setBusinessDescription(serviceProviderDto.getBusinessDescription());
        serviceProvider.setServiceProviderType(serviceProviderDto.getServiceProviderType());
        serviceProvider.setBusinessAddress(businessAddress);
        serviceProvider.setNumEmployees(serviceProviderDto.getNumEmployees());
        serviceProvider.setMaxTravelDistanceInKM(serviceProviderDto.getMaxTravelDistanceInKM());
        serviceProvider.setPortfolioType(serviceProviderDto.getPortfolioType());
        serviceProvider.setBusinessLogoUrl(serviceProviderDto.getBusinessLogoUrl());
        serviceProvider.setAverageRating(serviceProviderDto.getAverageRating());
        serviceProvider.setNumberOfReviews(serviceProviderDto.getNumberOfReviews());
        serviceProvider.setYearsOfExperience(serviceProviderDto.getYearsOfExperience());
        serviceProvider.setVerificationStatus(serviceProviderDto.getVerificationStatus());
        serviceProvider.setPortfolioUrl(serviceProviderDto.getPortfolioUrl());
        serviceProvider.setRegisteredAt(serviceProviderDto.getRegisteredAt());

        return serviceProvider;

    }


    public static ServiceProvider toEntity(ServiceProviderCreateDto serviceProviderDto, Address businessAddress) {
        if (serviceProviderDto == null) {
            return null;
        }

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setBusinessName(serviceProviderDto.getBusinessName());
        serviceProvider.setBusinessDescription(serviceProviderDto.getBusinessDescription());
        serviceProvider.setServiceProviderType(serviceProviderDto.getServiceProviderType());
        serviceProvider.setBusinessAddress(businessAddress);
        serviceProvider.setNumEmployees(serviceProviderDto.getNumEmployees());
        serviceProvider.setMaxTravelDistanceInKM(serviceProviderDto.getMaxTravelDistanceInKM());
        serviceProvider.setPortfolioType(serviceProviderDto.getPortfolioType());
        serviceProvider.setBusinessLogoUrl(serviceProviderDto.getBusinessLogoUrl());
        serviceProvider.setYearsOfExperience(serviceProviderDto.getYearsOfExperience());
        serviceProvider.setPortfolioUrl(serviceProviderDto.getPortfolioUrl());

        return serviceProvider;

    }


    public static ServiceProvider toEntity(ServiceProviderUpdateDto serviceProviderDto,
                                           ServiceProvider serviceProvider) {
        if (serviceProviderDto == null) {
            return null;
        }

        serviceProvider.setBusinessName(serviceProviderDto.getBusinessName());
        serviceProvider.setBusinessDescription(serviceProviderDto.getBusinessDescription());
        serviceProvider.setServiceProviderType(serviceProviderDto.getServiceProviderType());
        serviceProvider.setNumEmployees(serviceProviderDto.getNumEmployees());
        serviceProvider.setMaxTravelDistanceInKM(serviceProviderDto.getMaxTravelDistanceInKM());
        serviceProvider.setPortfolioType(serviceProviderDto.getPortfolioType());
        serviceProvider.setBusinessLogoUrl(serviceProviderDto.getBusinessLogoUrl());
        serviceProvider.setYearsOfExperience(serviceProviderDto.getYearsOfExperience());
        serviceProvider.setPortfolioUrl(serviceProviderDto.getPortfolioUrl());
        serviceProvider.setIsActive(serviceProviderDto.getIsActive());
        return serviceProvider;

    }

}
