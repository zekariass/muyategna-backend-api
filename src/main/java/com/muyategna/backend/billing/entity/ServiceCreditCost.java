package com.muyategna.backend.billing.entity;

import com.muyategna.backend.professional_service.entity.Service;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "payg_credits_per_lead", nullable = false)
    private BigDecimal paygCreditsPerLead;

    @Column(name = "subs_credits_per_lead", nullable = false)
    private BigDecimal subsCreditsPerLead;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}

