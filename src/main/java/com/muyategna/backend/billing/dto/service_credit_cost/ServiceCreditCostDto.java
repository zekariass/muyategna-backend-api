package com.muyategna.backend.billing.dto.service_credit_cost;


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
public class ServiceCreditCostDto {
    private Long id;
    private Long serviceId;
    private BigDecimal paygCreditsPerLead;
    private BigDecimal subsCreditsPerLead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
