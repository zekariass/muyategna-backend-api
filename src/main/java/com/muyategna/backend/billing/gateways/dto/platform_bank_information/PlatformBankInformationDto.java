package com.muyategna.backend.billing.gateways.dto.platform_bank_information;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformBankInformationDto {

    private Long id;

    private String bankName;

    private String bankAccountNumber;

    private String bankSortCode;

    private String swiftCode;

    private String iban;

    private String branchName;

    private String accountHolderName;

    private Long countryId;

    private String currency;

    private Boolean isActive;

    private Map<String, Object> extraData;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
