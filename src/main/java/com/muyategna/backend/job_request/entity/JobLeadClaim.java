package com.muyategna.backend.job_request.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_lead_claim")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobLeadClaim {

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
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private JobLeadClaimStatus status;

    @Column(name = "requote_requested")
    private Boolean requoteRequested = false;

    @CreatedDate
    @Column(name = "claimed_at")
    private LocalDateTime claimedAt;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobQuote> quotes;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    @JsonIgnore
    public boolean isWithdrawn() {
        return status == JobLeadClaimStatus.WITHDRAWN;
    }

    @Transient
    @JsonIgnore
    public boolean isExpired() {
        return status == JobLeadClaimStatus.EXPIRED;
    }


    @Transient
    @JsonIgnore
    public boolean isClaimed() {
        return status == JobLeadClaimStatus.CLAIMED;
    }

    @Transient
    @JsonIgnore
    public boolean isQuoted() {
        return status == JobLeadClaimStatus.QUOTED;
    }

    @Transient
    @JsonIgnore
    public boolean isAccepted() {
        return status == JobLeadClaimStatus.ACCEPTED;
    }

    @Transient
    @JsonIgnore
    public boolean isRejected() {
        return status == JobLeadClaimStatus.REJECTED;
    }

}


