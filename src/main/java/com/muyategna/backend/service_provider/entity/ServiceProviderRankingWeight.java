package com.muyategna.backend.service_provider.entity;

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
@Table(name = "service_provider_ranking_weights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProviderRankingWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "distance_weight", precision = 10, scale = 2)
    private BigDecimal distanceWeight;

    @Column(name = "total_claims_weight", precision = 10, scale = 2)
    private BigDecimal totalClaimsWeight;

    @Column(name = "avg_claim_time_in_mins_weight", precision = 10, scale = 2)
    private BigDecimal avgClaimTimeInMinsWeight;

    @Column(name = "avg_rating_weight", precision = 10, scale = 2)
    private BigDecimal avgRatingWeight;

    @Column(name = "completed_leads_weight", precision = 10, scale = 2)
    private BigDecimal completedLeadsWeight;

    @Column(name = "total_quotes_weight", precision = 10, scale = 2)
    private BigDecimal totalQuotesWeight;

    @Column(name = "accepted_quotes_weight", precision = 10, scale = 2)
    private BigDecimal acceptedQuotesWeight;

    @Column(name = "daily_claim_count_weight", precision = 10, scale = 2)
    private BigDecimal dailyClaimCountWeight;

    @Column(name = "weekly_claim_count_weight", precision = 10, scale = 2)
    private BigDecimal weeklyClaimCountWeight;

    @Column(name = "two_weekly_claim_count_weight", precision = 10, scale = 2)
    private BigDecimal twoWeeklyClaimCountWeight;

    @Column(name = "monthly_claim_count_weight", precision = 10, scale = 2)
    private BigDecimal monthlyClaimCountWeight;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


