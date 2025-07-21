package com.muyategna.backend.billing.entity;

import com.muyategna.backend.job_request.entity.JobLeadClaim;
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

@Entity
@Table(name = "service_provider_subscription_usage")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderSubscriptionUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private ServiceProviderSubscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_lead_claim_id")
    private JobLeadClaim jobLeadClaim;

    @Column(name = "used_credits", precision = 10, scale = 2, nullable = false)
    private BigDecimal usedCredits = BigDecimal.ZERO;

    @CreatedDate
    @Column(name = "used_at", nullable = false, updatable = false)
    private LocalDateTime usedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
