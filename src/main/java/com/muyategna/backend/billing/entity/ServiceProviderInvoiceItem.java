package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.ProductTypeEnum;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "service_provider_invoice_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProviderInvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_invoice_id")
    private ServiceProviderInvoice parentInvoice;

    @ManyToOne
    @JoinColumn(name = "tax_rule_id")
    private TaxRule taxRule;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductTypeEnum productType;

    private BigDecimal unitPrice;
    private Integer quantity = 1;
    private BigDecimal subTotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    @Column(name = "extra_data", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, String> extraData;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
