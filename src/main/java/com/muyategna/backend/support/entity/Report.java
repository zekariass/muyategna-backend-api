package com.muyategna.backend.support.entity;

import com.muyategna.backend.support.enums.ReportReason;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    private Long reportedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ReportReason reason;

    @Column(name = "other_reason")
    private String otherReason;

    @Column(name = "reportee_name", nullable = false, length = 255)
    private String reporteeName;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(name = "reported_at", updatable = false)
    private LocalDateTime reportedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

