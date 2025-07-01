package com.muyategna.backend.billing.entity;

import com.muyategna.backend.job_request.entity.ServiceProviderLead;
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
@Table(name = "add_on_usage")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddOnUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_provider_addon_id", nullable = false)
    private ServiceProviderAddOn serviceProviderAddon;

    @ManyToOne
    @JoinColumn(name = "service_provider_lead_id", nullable = false)
    private ServiceProviderLead serviceProviderLead;

    @Column(name = "used_credit_amount", nullable = false)
    private BigDecimal usedCreditAmount;

    @CreatedDate
    @Column(name = "used_at", nullable = false, updatable = false)
    private LocalDateTime usedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

