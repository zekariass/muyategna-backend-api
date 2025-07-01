package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.DiscountUserType;
import com.muyategna.backend.location.entity.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "discount_plan")
public class DiscountPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "fixed_value", precision = 15, scale = 4, nullable = false)
    private BigDecimal fixedValue;

    @Column(name = "percentage_value", precision = 15, scale = 4, nullable = false)
    private BigDecimal percentageValue;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "usage_limit", nullable = false)
    private Integer usageLimit = 0;

    @Column(name = "per_user_limit", nullable = false)
    private Integer perUserLimit = 0;

    @Column(name = "total_use_count", nullable = false)
    private Integer totalUseCount = 0;

    @Column(name = "max_discount_value", precision = 15, scale = 4)
    private BigDecimal maxDiscountValue;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "coupon_required", nullable = false)
    private Boolean couponRequired = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "applies_to", nullable = false)
    private DiscountUserType appliesTo;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "discountPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DiscountPlanTranslation> translations;
}
