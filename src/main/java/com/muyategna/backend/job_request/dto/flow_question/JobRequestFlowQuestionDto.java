package com.muyategna.backend.job_request.dto.flow_question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestFlowQuestionDto {
    private Long id;
    private Long flowId;
    private Long questionId;
    private Integer orderIndex;
    private boolean isStart;
    private boolean isTerminal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}