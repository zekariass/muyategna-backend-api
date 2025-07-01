package com.muyategna.backend.billing.dto.add_on_plan;

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
public class AddOnPlanDto {
    private Long id;
    //    private Long serviceId;
    private Long countryId;
    private String name;
    private BigDecimal priceAmount;
    private BigDecimal creditsIncluded;
    private Integer sortOrder;
    private LocalDateTime expiresAt;
    private Boolean isDefault;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

