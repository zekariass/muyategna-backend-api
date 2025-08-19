package com.muyategna.backend.billing.gateways.mappers.platform_bank_information;

import com.muyategna.backend.billing.entity.PlatformBankInformation;
import com.muyategna.backend.billing.gateways.dto.platform_bank_information.PlatformBankInformationDto;
import com.muyategna.backend.billing.gateways.dto.platform_bank_information.PlatformBankInformationViewDto;
import com.muyategna.backend.location.mapper.CountryMapper;

import java.util.List;

public final class PlatformBankInformationMapper {

    public static PlatformBankInformationDto toDto(PlatformBankInformation platformBankInformation) {
        if (platformBankInformation == null) {
            return null;
        }

        return PlatformBankInformationDto.builder()
                .id(platformBankInformation.getId())
                .bankName(platformBankInformation.getBankName())
                .bankAccountNumber(platformBankInformation.getBankAccountNumber())
                .accountHolderName(platformBankInformation.getAccountHolderName())
                .swiftCode(platformBankInformation.getSwiftCode())
                .iban(platformBankInformation.getIban())
                .branchName(platformBankInformation.getBranchName())
                .countryId(platformBankInformation.getCountry().getId())
                .currency(platformBankInformation.getCurrency())
                .isActive(platformBankInformation.getIsActive())
                .extraData(platformBankInformation.getExtraData())
                .createdAt(platformBankInformation.getCreatedAt())
                .updatedAt(platformBankInformation.getUpdatedAt())
                .build();
    }


    public static PlatformBankInformationViewDto toViewDto(PlatformBankInformation platformBankInformation) {
        if (platformBankInformation == null) {
            return null;
        }

        return PlatformBankInformationViewDto.builder()
                .id(platformBankInformation.getId())
                .bankName(platformBankInformation.getBankName())
                .bankAccountNumber(platformBankInformation.getBankAccountNumber())
                .bankSortCode(platformBankInformation.getBankSortCode())
                .accountHolderName(platformBankInformation.getAccountHolderName())
                .swiftCode(platformBankInformation.getSwiftCode())
                .iban(platformBankInformation.getIban())
                .branchName(platformBankInformation.getBranchName())
                .country(CountryMapper.toMinimalDto(platformBankInformation.getCountry()))
                .currency(platformBankInformation.getCurrency())
                .extraData(platformBankInformation.getExtraData())
                .build();
    }


    public static List<PlatformBankInformationViewDto> toViewDtoList(List<PlatformBankInformation> platformBankInformations) {
        if (platformBankInformations == null || platformBankInformations.isEmpty()) {
            return List.of();
        }
        return platformBankInformations.stream().map(PlatformBankInformationMapper::toViewDto).toList();
    }
}
