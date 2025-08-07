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
@Table(name = "service_provider_statistics")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProviderStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_service_provider_statistics_provider"))
    private ServiceProvider provider;

    @Column(name = "total_claims", nullable = false)
    private Integer totalClaims = 0;

    @Column(name = "avg_claim_time_in_mins", precision = 15, scale = 4, nullable = false)
    private BigDecimal avgClaimTimeInMins = BigDecimal.ZERO;

    @Column(name = "avg_rating", precision = 2, scale = 2, nullable = false)
    private BigDecimal avgRating = BigDecimal.ZERO;

    @Column(name = "completed_leads", nullable = false)
    private Integer completedLeads = 0;

    @Column(name = "total_quotes", nullable = false)
    private Integer totalQuotes = 0;

    @Column(name = "accepted_quotes", nullable = false)
    private Integer acceptedQuotes = 0;

    @Column(name = "daily_claim_count", nullable = false)
    private Integer dailyClaimCount = 0;

    @Column(name = "weekly_claim_count", nullable = false)
    private Integer weeklyClaimCount = 0;

    @Column(name = "two_weekly_claim_count", nullable = false)
    private Integer twoWeeklyClaimCount = 0;

    @Column(name = "monthly_claim_count", nullable = false)
    private Integer monthlyClaimCount = 0;

    @Column(name = "total_new_job_notifs", nullable = false)
    private Integer totalNewJobNotifs = 0;

    @Column(name = "daily_new_job_notifs", nullable = false)
    private Integer dailyNewJobNotifs = 0;

    @Column(name = "weekly_new_job_notifs", nullable = false)
    private Integer weeklyNewJobNotifs = 0;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}

