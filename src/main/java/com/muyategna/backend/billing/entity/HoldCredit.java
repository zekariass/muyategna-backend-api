package com.muyategna.backend.billing.entity;


import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.billing.enums.HoldStatus;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hold_credit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class HoldCredit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_wallet_id")
    private ServiceProviderWallet providerWallet;

    @ManyToOne
    @JoinColumn(name = "job_lead_claim_id")
    private JobLeadClaim jobLeadClaim;

    @Column(name = "hold_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal holdAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CreditSource creditSource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private HoldStatus status = HoldStatus.ACTIVE;

    @CreatedDate
    private LocalDateTime holdAt;

    private LocalDateTime releasedAt;
    private LocalDateTime convertedAt;
    private LocalDateTime expiredAt;

    public HoldCredit(ServiceProviderWallet serviceProviderWallet, BigDecimal acceptanceCost, CreditSource creditSource) {
        this.setHoldAmount(acceptanceCost);
        this.setProviderWallet(serviceProviderWallet);
        this.setCreditSource(creditSource);
        this.setStatus(HoldStatus.ACTIVE);
        this.setHoldAt(LocalDateTime.now());
    }
}
