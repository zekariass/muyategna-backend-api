package com.muyategna.backend.questionnaire.entity;

import com.muyategna.backend.questionnaire.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobQuestion {
    @Id
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "flow_id")
    private JobQuestionFlow flow;

    private String question;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


}

