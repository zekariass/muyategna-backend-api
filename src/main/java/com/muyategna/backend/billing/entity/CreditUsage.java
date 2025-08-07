package com.muyategna.backend.billing.entity;

import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "credit_usage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CreditUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @ManyToOne
    @JoinColumn(name = "job_lead_claim_id")
    private JobLeadClaim jobLeadClaim;

    @Column(name = "used_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal usedAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CreditSource creditSource;

    @Column(name = "is_partial", nullable = false)
    private Boolean isPartial;

    @Column(name = "extra_data", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, String> extraData;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    public CreditUsage(ServiceProvider serviceProvider, JobLeadClaim savedJobLeadClaim, BigDecimal usedCreditAmount, CreditSource creditSource, Boolean isPartial) {
        this.setProvider(serviceProvider);
        this.setJobLeadClaim(savedJobLeadClaim);
        this.setUsedAmount(usedCreditAmount);
        this.setCreditSource(creditSource);
        this.setIsPartial(isPartial);
        this.setUsedAt(LocalDateTime.now());
    }
}
