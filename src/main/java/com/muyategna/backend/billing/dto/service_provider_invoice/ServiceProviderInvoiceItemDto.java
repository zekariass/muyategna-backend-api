package com.muyategna.backend.billing.dto.service_provider_invoice;

import com.muyategna.backend.billing.enums.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderInvoiceItemDto {
    private Long id;
    private Long parentInvoiceId;
    private Long taxRuleId;
    private String description;
    private ProductTypeEnum productType;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subTotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
}

