package com.muyategna.backend.billing.entity;

import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "service_provider_credit_usage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceProviderCreditUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "usage_ref", nullable = false, unique = true, length = 50)
//    private String usageRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_lead_claim_id", nullable = false)
    private JobLeadClaim jobLeadClaim;

    @Column(name = "used_credit_amount", nullable = false, precision = 15, scale = 4)
    private BigDecimal usedCreditAmount;

    @Type(JsonBinaryType.class)
    @Column(name = "extra_data", columnDefinition = "jsonb")
    private Map<String, Object> extraData;

    @CreatedDate
    @Column(name = "used_at", updatable = false)
    private LocalDateTime usedAt;
}
