package com.muyategna.backend.professional_service.dto.availability;

import com.muyategna.backend.professional_service.enums.PriceModel;
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
public class ServiceCountryAvailabilityDto {
    private Long id;
    private Long serviceId;
    private Long countryId;
    private Boolean isActive;
    private PriceModel priceModel;
    private BigDecimal basePrice;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
