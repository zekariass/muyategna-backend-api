package com.muyategna.backend.billing.dto.service_credit_cost;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private BigDecimal paygClaimCostPerLead;
    private BigDecimal paygAcceptanceCostPerLead;
    private BigDecimal subsClaimCostPerLead;
    private BigDecimal subsAcceptanceCostPerLead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    public boolean isFree() {
        return paygClaimCostPerLead.compareTo(BigDecimal.ZERO) == 0
                && paygAcceptanceCostPerLead.compareTo(BigDecimal.ZERO) == 0
                && subsClaimCostPerLead.compareTo(BigDecimal.ZERO) == 0
                && subsAcceptanceCostPerLead.compareTo(BigDecimal.ZERO) == 0;
    }
}
