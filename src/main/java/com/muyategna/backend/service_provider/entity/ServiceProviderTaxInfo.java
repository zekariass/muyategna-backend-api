package com.muyategna.backend.service_provider.entity;

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
@Table(name = "service_provider_tax_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProviderTaxInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "service_provider_id", nullable = false, unique = true)
    private ServiceProvider serviceProvider;

    @Column(name = "tax_payer_id_number", length = 50)
    private String taxPayerIdNumber;

    @Column(name = "is_vat_registered", nullable = false)
    private Boolean isVatRegistered = false;

    @Column(name = "is_tax_exempt", nullable = false)
    private Boolean isTaxExempt = false;

    @Column(name = "tax_exempt_certificate_number", length = 50)
    private String taxExemptCertificateNumber;

    @Column(name = "tax_exemption_reason", columnDefinition = "TEXT")
    private String taxExemptionReason;

    @Column(name = "income_tax_classification", length = 50)
    private String incomeTaxClassification;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}