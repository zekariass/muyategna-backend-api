package com.muyategna.backend.questionnaire.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "job_question_transition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobQuestionTransition {
    @Id
    private UUID transitionId;

    @ManyToOne
    @JoinColumn(name = "current_question_id")
    private JobQuestion currentQuestion;

    @ManyToOne
    @JoinColumn(name = "question_option_id", nullable = true)
    private JobQuestionOption questionOption;

    @ManyToOne
    @JoinColumn(name = "next_question_id")
    private JobQuestion nextQuestion;
}
