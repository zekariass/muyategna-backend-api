package com.muyategna.backend.job_request.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Represents a question_transition in the job request flow, linking current and next questions,
 * with an optional condition expression or option that triggers the question_transition.
 */

@Entity
@Table(name = "job_question_transition")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobQuestionTransition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The parent flow
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id", nullable = false)
    private JobRequestFlow flow;

    // This is the question we are currently on
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_flow_question_id", nullable = false)
    private JobRequestFlowQuestion fromFlowQuestion;

    // The question to jump to next if the condition matches
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_flow_question_id", nullable = false)
    private JobRequestFlowQuestion toFlowQuestion;

    // Option chosen to trigger question_transition (can be null — for conditionExpression or linear next)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private JobQuestionOption option;

    @Column(name = "condition_expression", columnDefinition = "TEXT")
    private String conditionExpression;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
