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
import java.util.List;

/**
 * Represents a question in a job request flow.
 * This entity links a job request flow to a job question and contains metadata about the question's position
 * in the flow, whether it is a starting or terminal question, and any associated answers.
 */

@Entity
@Table(name = "job_request_flow_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobRequestFlowQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The parent flow this question belongs to
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id", nullable = false, foreignKey = @ForeignKey(name = "fk_job_request_flow_question_flow"))
    private JobRequestFlow flow;

    // The job question that this flow question refers to
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "fk_job_request_flow_question_question"))
    private JobQuestion question;

    // The index of this question in the flow, used to determine the order of questions
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    // Indicates whether this question is the starting point of the flow
    @Column(name = "is_start", nullable = false)
    private boolean isStart = false;

    // Indicates whether this question is the terminal point of the flow
    @Column(name = "is_terminal", nullable = false)
    private boolean isTerminal = false;

    // List of answers associated with this flow question
    @OneToMany(mappedBy = "flowQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<JobQuestionAnswer> answers;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}