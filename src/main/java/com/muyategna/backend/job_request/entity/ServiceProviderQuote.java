package com.muyategna.backend.job_request.entity;

import com.muyategna.backend.job_request.enums.ServiceProviderQuoteStatus;
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
@Table(name = "service_provider_quote", indexes = {
        @Index(name = "idx_spq_provider_lead_id", columnList = "provider_lead_id"),
        @Index(name = "idx_spq_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_lead_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_spq_provider_lead"))
    private ServiceProviderLead providerLead;

    @Column(name = "proposed_price", precision = 15, scale = 4)
    private BigDecimal proposedPrice;

    @Column(name = "proposed_start_date")
    private LocalDateTime proposedStartDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceProviderQuoteStatus status;

    private LocalDateTime acceptedAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
