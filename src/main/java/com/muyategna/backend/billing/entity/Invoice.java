package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.InvoiceStatusEnum;
import com.muyategna.backend.billing.enums.ProductType;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.entity.Region;
import io.hypersistence.utils.hibernate.type.array.LongArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "invoice")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "payer_entity_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UserEntityType payerEntityType;

    @Column(name = "payer_entity_id", nullable = false)
    private Long payerEntityId;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ProductType productType;

    @Column(name = "product_plan_id", nullable = false)
    private Long productPlanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_plan_id", nullable = true)
    private DiscountPlan discountPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = true)
    private Coupon coupon;

    @Column(name = "coupon_code", length = 100, nullable = true)
    private String couponCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = true)
    private Region region;

    private String note;

    @Column(nullable = false)
    private BigDecimal subTotalAmount = BigDecimal.ZERO;

    @Column(name = "tax_rule_ids", columnDefinition = "bigint[]")
    @Type(LongArrayType.class)
    private Long[] taxRuleIds;

    @Column(nullable = false)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "credits_included", nullable = false)
    private BigDecimal creditsIncluded = BigDecimal.ZERO;

    @Column(length = 3, nullable = false)
    private String currency;

    private LocalDateTime dueDate;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private InvoiceStatusEnum status;

    @Column(nullable = false)
    private boolean isLocked = false;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, Object> extraData;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
