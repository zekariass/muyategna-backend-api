package com.muyategna.backend.job_request.dto.request_flow;

import com.muyategna.backend.job_request.dto.flow_question.JobRequestFlowQuestionMinimalDto;
import com.muyategna.backend.job_request.dto.question_transition.JobQuestionTransitionMinimalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for public representation of a job request flow.
 * Contains flow ID, service ID, associated questions, and transitions.
 * This DTO is used to expose the job request flow details to the public API.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestFlowPublicDto {
    private Long id;
    private Long serviceId;
    private List<JobRequestFlowQuestionMinimalDto> flowQuestions;
    private List<JobQuestionTransitionMinimalDto> transitions;
}
