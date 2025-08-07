package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.service_provider.dto.service_provider.*;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceLocalizedDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.repository.projection.NearbyServiceProviderForAJobProjection;

import java.util.List;
import java.util.stream.Collectors;

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
                .businessEmail(serviceProvider.getBusinessEmail())
                .businessPhone(serviceProvider.getBusinessPhone())
                .website(serviceProvider.getWebsite())
                .tiktok(serviceProvider.getTiktok())
                .whatsApp(serviceProvider.getWhatsapp())
                .facebook(serviceProvider.getFacebook())
                .linkedin(serviceProvider.getLinkedin())
                .instagram(serviceProvider.getInstagram())
                .defaultLanguageId(serviceProvider.getDefaultLanguage().getId())
                .registeredAt(serviceProvider.getRegisteredAt())
                .build();
    }


    public static NearbyServiceProviderListDto toListDto(ServiceProvider serviceProvider) {
        if (serviceProvider == null) {
            return null;
        }

        return NearbyServiceProviderListDto.builder()
                .id(serviceProvider.getId())
                .businessName(serviceProvider.getBusinessName())
                .businessDescription(serviceProvider.getBusinessDescription())
                .fullAddress(serviceProvider.getBusinessAddress().getFullAddress())
                .maxTravelDistanceInKM(serviceProvider.getMaxTravelDistanceInKM())
                .businessLogoUrl(serviceProvider.getBusinessLogoUrl())
                .averageRating(serviceProvider.getAverageRating())
                .numberOfReviews(serviceProvider.getNumberOfReviews())
                .yearsOfExperience(serviceProvider.getYearsOfExperience())
                .businessEmail(serviceProvider.getBusinessEmail())
                .businessPhone(serviceProvider.getBusinessPhone())
                .build();
    }


    public static ServiceProviderDetailDto toDetailDto(ServiceProvider serviceProvider, List<ServiceProviderServiceLocalizedDto> subscribedServices) {
        if (serviceProvider == null) {
            return null;
        }

        return ServiceProviderDetailDto.builder()
                .id(serviceProvider.getId())
                .businessName(serviceProvider.getBusinessName())
                .businessDescription(serviceProvider.getBusinessDescription())
                .serviceProviderType(serviceProvider.getServiceProviderType())
                .fullAddress(serviceProvider.getBusinessAddress().getFullAddress())
                .numEmployees(serviceProvider.getNumEmployees())
                .maxTravelDistanceInKM(serviceProvider.getMaxTravelDistanceInKM())
                .portfolioUrl(serviceProvider.getPortfolioUrl())
                .portfolioType(serviceProvider.getPortfolioType())
                .businessLogoUrl(serviceProvider.getBusinessLogoUrl())
                .verificationStatus(serviceProvider.getVerificationStatus())
                .averageRating(serviceProvider.getAverageRating())
                .numberOfReviews(serviceProvider.getNumberOfReviews())
                .yearsOfExperience(serviceProvider.getYearsOfExperience())
                .isVerified(serviceProvider.getIsVerified())
                .businessEmail(serviceProvider.getBusinessEmail())
                .businessPhone(serviceProvider.getBusinessPhone())
                .tiktok(serviceProvider.getTiktok())
                .whatsApp(serviceProvider.getWhatsapp())
                .facebook(serviceProvider.getFacebook())
                .linkedin(serviceProvider.getLinkedin())
                .instagram(serviceProvider.getInstagram())
                .website(serviceProvider.getWebsite())
                .defaultLanguageId(serviceProvider.getDefaultLanguage().getId())
                .subscribedServices(subscribedServices)
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
        serviceProvider.setBusinessEmail(serviceProviderDto.getBusinessEmail());
        serviceProvider.setBusinessPhone(serviceProviderDto.getBusinessPhone());
        serviceProvider.setWebsite(serviceProviderDto.getWebsite());
        serviceProvider.setTiktok(serviceProviderDto.getTiktok());
        serviceProvider.setWhatsapp(serviceProviderDto.getWhatsApp());
        serviceProvider.setFacebook(serviceProviderDto.getFacebook());
        serviceProvider.setLinkedin(serviceProviderDto.getLinkedin());
        serviceProvider.setInstagram(serviceProviderDto.getInstagram());
        serviceProvider.setIsActive(serviceProviderDto.getIsActive());
        serviceProvider.setIsVerified(serviceProviderDto.getIsVerified());
        serviceProvider.setIsBlocked(serviceProviderDto.getIsBlocked());
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
        serviceProvider.setBusinessEmail(serviceProviderDto.getBusinessEmail());
        serviceProvider.setBusinessPhone(serviceProviderDto.getBusinessPhone());
        serviceProvider.setIsActive(true);

        Language language = new Language();
        language.setId(serviceProviderDto.getDefaultLanguageId());

        serviceProvider.setDefaultLanguage(language);

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
        serviceProvider.setBusinessEmail(serviceProviderDto.getBusinessEmail());
        serviceProvider.setBusinessPhone(serviceProviderDto.getBusinessPhone());
        serviceProvider.setWebsite(serviceProviderDto.getWebsite());
        serviceProvider.setTiktok(serviceProviderDto.getTiktok());
        serviceProvider.setWhatsapp(serviceProviderDto.getWhatsApp());
        serviceProvider.setFacebook(serviceProviderDto.getFacebook());
        serviceProvider.setLinkedin(serviceProviderDto.getLinkedin());
        serviceProvider.setInstagram(serviceProviderDto.getInstagram());
        return serviceProvider;

    }


    public static NearbyServiceProviderListDto toNearbyProviderDto(NearbyServiceProviderForAJobProjection projection) {
        if (projection == null) {
            return null;
        }

        return NearbyServiceProviderListDto.builder()
                .id(projection.getServiceProviderId())
                .businessName(projection.getBusinessName())
                .businessDescription(projection.getBusinessDescription())
                .fullAddress(projection.getFullAddress())
                .maxTravelDistanceInKM(projection.getMaxTravelDistanceInKM())
                .businessLogoUrl(projection.getBusinessLogoUrl())
                .averageRating(projection.getAverageRating())
                .numberOfReviews(projection.getNumberOfReviews())
                .yearsOfExperience(projection.getYearsOfExperience())
                .businessEmail(projection.getBusinessEmail())
                .businessPhone(projection.getBusinessPhone())
                .inviteForQuoteId(projection.getInviteForQuoteId())
                .claimId(projection.getClaimId())
                .distanceFromJobLocation(projection.getDistanceFromJobInMeters())
                .build();
    }


    public static ServiceProviderListCustomerViewDto toServiceProviderCustomerViewDto(ServiceProvider serviceProvider) {
        if (serviceProvider == null) {
            return null;
        }

        return ServiceProviderListCustomerViewDto.builder()
                .id(serviceProvider.getId())
                .businessName(serviceProvider.getBusinessName())
                .businessDescription(serviceProvider.getBusinessDescription())
                .fullAddress(serviceProvider.getBusinessAddress().getFullAddress())
                .maxTravelDistanceInKM(serviceProvider.getMaxTravelDistanceInKM())
                .businessLogoUrl(serviceProvider.getBusinessLogoUrl())
                .averageRating(serviceProvider.getAverageRating())
                .numberOfReviews(serviceProvider.getNumberOfReviews())
                .yearsOfExperience(serviceProvider.getYearsOfExperience())
                .isVerified(serviceProvider.getIsVerified())
                .businessEmail(serviceProvider.getBusinessEmail())
                .businessPhone(serviceProvider.getBusinessPhone())
                .build();
    }


    public static ServiceProviderDetailCustomerViewDto toDetailCustomerViewDto(ServiceProvider serviceProvider, List<ServiceProviderServiceLocalizedDto> subscribedServices) {
        if (serviceProvider == null) {
            return null;
        }

        return ServiceProviderDetailCustomerViewDto.builder()
                .id(serviceProvider.getId())
                .businessName(serviceProvider.getBusinessName())
                .businessDescription(serviceProvider.getBusinessDescription())
                .fullAddress(serviceProvider.getBusinessAddress().getFullAddress())
                .maxTravelDistanceInKM(serviceProvider.getMaxTravelDistanceInKM())
                .businessLogoUrl(serviceProvider.getBusinessLogoUrl())
                .averageRating(serviceProvider.getAverageRating())
                .numberOfReviews(serviceProvider.getNumberOfReviews())
                .yearsOfExperience(serviceProvider.getYearsOfExperience())
                .isVerified(serviceProvider.getIsVerified())
                .businessEmail(serviceProvider.getBusinessEmail())
                .businessPhone(serviceProvider.getBusinessPhone())
                .facebook(serviceProvider.getFacebook())
                .instagram(serviceProvider.getInstagram())
                .linkedin(serviceProvider.getLinkedin())
                .tiktok(serviceProvider.getTiktok())
                .website(serviceProvider.getWebsite())
                .serviceProviderType(serviceProvider.getServiceProviderType())
                .portfolioUrl(serviceProvider.getPortfolioUrl())
                .portfolioType(serviceProvider.getPortfolioType())
                .whatsApp(serviceProvider.getWhatsapp())
                .build();
    }


    public static List<NearbyServiceProviderListDto> toNearbyProviderListDto(List<NearbyServiceProviderForAJobProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }

        return projections.stream().map(ServiceProviderMapper::toNearbyProviderDto).collect(Collectors.toList());

    }

}
