package com.muyategna.backend.request_and_lead.entity;

import com.muyategna.backend.request_and_lead.enums.RequestStatus;
import com.muyategna.backend.request_and_lead.enums.StartDateRange;
import com.muyategna.backend.user.entity.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Table(name = "job_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;

    @Column(nullable = false)
    private long serviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserProfile customer;

    @Column(length = 1000)
    private String requestDescription;

    @Column(precision = 10, scale = 2)
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(name = "start_date_range", nullable = false)
    private StartDateRange startDateRange;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(length = 255)
    private String location;

}
