package com.muyategna.backend.job_request.entity;

import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.enums.WhenToStartJob;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.user.entity.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a job request made by a customer for a specific service.
 * This entity contains details about the job request, including the service,
 * customer, description, budget, when to start, status, address, and answers to job questions.
 */

@Entity
@Table(name = "job_request")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserProfile customer;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "budget")
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(name = "when_to_start_job", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private WhenToStartJob whenToStartJob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private JobStatus status = JobStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "jobRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JobQuestionAnswer> answers;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
