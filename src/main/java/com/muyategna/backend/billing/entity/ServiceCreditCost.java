package com.muyategna.backend.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muyategna.backend.professional_service.entity.Service;
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
@Table(name = "service_credit_cost")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreditCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "service_id", nullable = false, unique = true)
    private Service service;

    @Column(name = "payg_claim_cost_per_lead", nullable = false)
    private BigDecimal paygClaimCostPerLead;

    @Column(name = "payg_acceptance_cost_per_lead", nullable = false)
    private BigDecimal paygAcceptanceCostPerLead;

    @Column(name = "subs_claim_cost_per_lead", nullable = false)
    private BigDecimal subsClaimCostPerLead;

    @Column(name = "subs_acceptance_cost_per_lead", nullable = false)
    private BigDecimal subsAcceptanceCostPerLead;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JsonIgnore
    public boolean isFree() {
        return paygClaimCostPerLead.compareTo(BigDecimal.ZERO) == 0
                && paygAcceptanceCostPerLead.compareTo(BigDecimal.ZERO) == 0
                && subsClaimCostPerLead.compareTo(BigDecimal.ZERO) == 0
                && subsAcceptanceCostPerLead.compareTo(BigDecimal.ZERO) == 0;
    }

}

