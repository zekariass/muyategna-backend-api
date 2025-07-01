package com.muyategna.backend.service_provider.entity;

import com.muyategna.backend.service_provider.enums.RefereeActionStatus;
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
@Table(name = "referral_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReferralEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participation_id", nullable = false)
    private ReferralParticipation participation;

    @ManyToOne
    @JoinColumn(name = "referee_id", nullable = false)
    private ServiceProvider referee;

    @Enumerated(EnumType.STRING)
    @Column(name = "referee_action_status", nullable = false)
    private RefereeActionStatus refereeActionStatus = RefereeActionStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
