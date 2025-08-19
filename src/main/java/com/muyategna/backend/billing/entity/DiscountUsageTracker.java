package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.UserEntityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "discount_usage_tracker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DiscountUsageTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "provider_id", nullable = false)
//    private ServiceProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_user_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UserEntityType discountUserType;

    @Column(name = "discount_user_id", nullable = false)
    private Long discountUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private DiscountPlan discountPlan;

    @Column(nullable = false)
    private Integer usageLimit;

    @Column(nullable = false)
    private Integer useCount = 0;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime firstUsedAt;

    private LocalDateTime lastUsedAt;
}
