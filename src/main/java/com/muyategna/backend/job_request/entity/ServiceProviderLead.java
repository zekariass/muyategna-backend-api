package com.muyategna.backend.job_request.entity;

import com.muyategna.backend.job_request.enums.ServiceProviderLeadStatus;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
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
@Table(name = "service_provider_lead", indexes = {
        @Index(name = "idx_spl_job_lead_id", columnList = "job_lead_id"),
        @Index(name = "idx_spl_provider_id", columnList = "provider_id"),
        @Index(name = "idx_spl_job_lead_id_provider_id_status", columnList = "job_lead_id, provider_id, status"),
        @Index(name = "idx_spl_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_lead_id", nullable = false)
    private JobLead jobLead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceProviderLeadStatus status;

    @Column(name = "response_duration")
    private Long responseDuration;

    private LocalDateTime viewedAt;
    private LocalDateTime respondedAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
