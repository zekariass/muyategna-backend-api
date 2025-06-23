package com.muyategna.backend.job_request.dto.flow_question;

import com.muyategna.backend.job_request.dto.job_question.JobQuestionMinimalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestFlowQuestionMinimalDto {
    private Long id;
    private Long flowId;
    private JobQuestionMinimalDto question;
    private Integer orderIndex;
    private boolean isStart;
    private boolean isTerminal;
}