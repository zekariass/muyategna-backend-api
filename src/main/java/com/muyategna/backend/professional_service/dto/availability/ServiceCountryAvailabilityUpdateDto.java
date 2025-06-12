package com.muyategna.backend.professional_service.dto.availability;

import com.muyategna.backend.professional_service.enums.PriceModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCountryAvailabilityUpdateDto {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Service cannot be null")
    private Long service;

    @NotNull(message = "Country cannot be null")
    private Long country;
    private Boolean isActive;

    @NotNull(message = "Price model cannot be null")
    private PriceModel priceModel;

    @NotNull(message = "Base price cannot be null")
    private BigDecimal basePrice;
    private String notes;
}
