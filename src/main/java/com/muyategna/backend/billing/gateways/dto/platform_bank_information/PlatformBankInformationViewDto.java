package com.muyategna.backend.billing.gateways.dto.platform_bank_information;

import com.muyategna.backend.location.dto.country.CountryMinimalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformBankInformationViewDto {

    private Long id;

    private String bankName;

    private String bankAccountNumber;

    private String bankSortCode;

    private String swiftCode;

    private String iban;

    private String branchName;

    private String accountHolderName;

    private CountryMinimalDto country;

    private String currency;

    private Map<String, Object> extraData;
}
