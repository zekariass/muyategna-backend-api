package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.RenewalStatusEnum;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "service_provider_subscription")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upgraded_from")
    private ServiceProviderSubscription upgradedFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downgraded_from")
    private ServiceProviderSubscription downgradedFrom;

    @Enumerated(EnumType.STRING)
    @Column(name = "renewal_status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private RenewalStatusEnum renewalStatus;

    @Type(JsonBinaryType.class)
    @Column(name = "extra_data", columnDefinition = "jsonb")
    private Map<String, Object> extraData;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "initial_credits", nullable = false)
    private BigDecimal initialCredits;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "auto_renew", nullable = false)
    private Boolean autoRenew = true;

    @Column(name = "next_billing_date")
    private LocalDateTime nextBillingDate;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "renewal_attempted_at")
    private LocalDateTime renewalAttemptedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "last_billing_date")
    private LocalDateTime lastBillingDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "trial_period_started_at")
    private LocalDateTime trialPeriodStartedAt;

    @Column(name = "trial_period_ends_at")
    private LocalDateTime trialPeriodEndsAt;

    @Column(name = "grace_period_started_at")
    private LocalDateTime gracePeriodStartedAt;

    @Column(name = "grace_period_ends_at")
    private LocalDateTime gracePeriodEndsAt;


    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();

        boolean inSubscriptionPeriod = startedAt != null && endDate != null &&
                !now.isBefore(startedAt) && !now.isAfter(endDate);

        boolean inTrialPeriod = trialPeriodStartedAt != null && trialPeriodEndsAt != null &&
                !now.isBefore(trialPeriodStartedAt) && !now.isAfter(trialPeriodEndsAt);

        boolean inGracePeriod = gracePeriodStartedAt != null && gracePeriodEndsAt != null &&
                !now.isBefore(gracePeriodStartedAt) && !now.isAfter(gracePeriodEndsAt);

        return Boolean.TRUE.equals(isActive) && (inSubscriptionPeriod || inTrialPeriod || inGracePeriod);
    }
}



