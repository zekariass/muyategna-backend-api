package com.muyategna.backend.job_request.dto.flow_question;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestFlowQuestionCreateDto {
    @NotNull(message = "Flow ID cannot be null")
    private Long flowId;

    @NotNull(message = "Question ID cannot be null")
    private Long questionId;

    @NotNull(message = "Order index cannot be null")
    private Integer orderIndex;

    @NotNull(message = "Is start flag cannot be null")
    private Boolean isStart;

    @NotNull(message = "Is terminal flag cannot be null")
    private Boolean isTerminal;
}