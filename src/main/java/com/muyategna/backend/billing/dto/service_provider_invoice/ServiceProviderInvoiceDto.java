package com.muyategna.backend.billing.dto.service_provider_invoice;

import com.muyategna.backend.billing.enums.InvoiceStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderInvoiceDto {
    private Long id;
    private Long providerId;
    private Long providerTaxInfoId;
    private Long transactionId;
    private Long countryId;
    private Long regionId;
    private String description;
    private BigDecimal subTotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String currency;
    private InvoiceStatusEnum status;
    private Boolean isLocked;
    private LocalDateTime issuedAt;
    private List<ServiceProviderInvoiceItemDto> items;
}
