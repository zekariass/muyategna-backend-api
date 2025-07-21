package com.muyategna.backend.job_request.entity;

import com.muyategna.backend.common.entity.Language;
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
@Table(name = "rejection_reason_translation")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RejectionReasonTranslation {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "reason_id", nullable = false)
        private RejectionReason reason;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "language_id",  nullable = false)
        private Language language;

        @Column(name = "display_title", length = 100, nullable = false)
        private String displayTitle;

        @Column(columnDefinition = "TEXT")
        private String description;

        @CreatedDate
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @LastModifiedDate
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
}