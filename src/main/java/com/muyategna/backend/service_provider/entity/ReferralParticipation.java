package com.muyategna.backend.service_provider.entity;

import com.muyategna.backend.service_provider.enums.ReferralParticipationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "referral_participation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReferralParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "referrer_id", nullable = false)
    private ServiceProvider referrer;

    @ManyToOne
    @JoinColumn(name = "referral_plan_id", nullable = false)
    private ReferralPlan referralPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ReferralParticipationStatus status = ReferralParticipationStatus.OPEN;

    @CreatedDate
    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

