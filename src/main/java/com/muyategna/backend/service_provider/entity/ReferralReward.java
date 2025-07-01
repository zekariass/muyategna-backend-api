package com.muyategna.backend.service_provider.entity;

import com.muyategna.backend.billing.entity.Coupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "referral_reward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReferralReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @ManyToOne
    @JoinColumn(name = "participation_id", nullable = false)
    private ReferralParticipation participation;

    @Column(name = "issued_manually", nullable = false)
    private Boolean issuedManually = false;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @CreatedDate
    @Column(name = "issued_at", updatable = false)
    private LocalDateTime issuedAt;
}
