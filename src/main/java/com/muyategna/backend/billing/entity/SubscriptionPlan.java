package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.BillingCycleEnum;
import com.muyategna.backend.location.entity.Country;
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
@Table(name = "subscription_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_amount", precision = 15, scale = 4, nullable = false)
    private BigDecimal priceAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    private BillingCycleEnum billingCycle = BillingCycleEnum.MONTH; // Consider changing to days only

    @Column(name = "billing_cycle_count", nullable = false)
    private Integer billingCycleCount = 1;

    @Column(name = "trial_period_days", nullable = false)
    private Integer trialPeriodDays = 0;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "credits_included", nullable = false)
    private BigDecimal creditsIncluded = BigDecimal.ZERO;

    @Column(name = "upgradeable", nullable = false)
    private Boolean upgradeable = true;

    @Column(name = "downgradeable", nullable = false)
    private Boolean downgradeable = true;

    @Column(name = "extra_data", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, Object> extraData;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
