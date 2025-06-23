package com.muyategna.backend.job_request.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents an answer to a job question in the job request flow.
 * This entity captures the user's response to a specific question in the job request process.
 */

@Entity
@Table(name = "job_question_answers")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobQuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_request_id", nullable = false)
    private JobRequest jobRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_question_id", nullable = false)
    private JobRequestFlowQuestion flowQuestion;

    // The answer can be a text, number, boolean, date, or a list of options
    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "answer_number", precision = 10, scale = 2)
    private BigDecimal answerNumber;

    @Column(name = "answer_boolean")
    private Boolean answerBoolean;

    @Column(name = "answer_date")
    private LocalDate answerDate;

    @Column(name = "answer_option_ids", columnDefinition = "BIGINT[]")
    @JdbcTypeCode(SqlTypes.ARRAY) // Hibernate 6+ way
    private Long[] answerOptionIds;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}