package com.muyategna.backend.billing.dto.tax_rule;

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
public class TaxRuleCreateDto {
    private Long id;
    private Long countryId;
    private Long regionId;
    private String taxType;
    private String name;
    private String description;
    private BigDecimal percentageValue;
    private BigDecimal fixedValue;
    private Boolean isActive;
    private LocalDateTime effectiveStartDate;
    private LocalDateTime effectiveEndDate;
}
