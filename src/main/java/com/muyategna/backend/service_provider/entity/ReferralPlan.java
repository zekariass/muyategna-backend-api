package com.muyategna.backend.service_provider.entity;

import com.muyategna.backend.billing.entity.DiscountPlan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "referral_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReferralPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "referrer_discount_plan_id")
    private DiscountPlan referrerDiscountPlan;

    @ManyToOne
    @JoinColumn(name = "referee_discount_plan_id")
    private DiscountPlan refereeDiscountPlan;

    @Column(name = "number_of_referrals_required", nullable = false)
    private Integer numberOfReferralsRequired;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    private LocalDateTime startsAt;
    private LocalDateTime expiresAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

