package com.muyategna.backend.job_request.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muyategna.backend.job_request.enums.JobQuoteStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_quote")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private JobLeadClaim claim;

    @Column(name = "proposed_price", precision = 15, scale = 2)
    private BigDecimal proposedPrice;

    @Column(name = "proposed_start_date")
    private LocalDateTime proposedStartDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private JobQuoteStatus status;

    @Column(name = "customer_feedback", columnDefinition = "TEXT")
    private String customerFeedback;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Transient
    @JsonIgnore
    public boolean isAccepted() {
        return status == JobQuoteStatus.ACCEPTED;
    }

    @Transient
    @JsonIgnore
    public boolean isRejected() {
        return status == JobQuoteStatus.REJECTED;
    }

    @Transient
    @JsonIgnore
    public boolean isWithdrawn() {
        return status == JobQuoteStatus.WITHDRAWN;
    }


    @Transient
    @JsonIgnore
    public boolean isPending() {
        return status == JobQuoteStatus.PENDING;
    }
}


