package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.question_transition.JobQuestionTransitionCreateDto;
import com.muyategna.backend.job_request.dto.question_transition.JobQuestionTransitionDto;
import com.muyategna.backend.job_request.dto.question_transition.JobQuestionTransitionMinimalDto;
import com.muyategna.backend.job_request.dto.question_transition.JobQuestionTransitionUpdateDto;
import com.muyategna.backend.job_request.entity.JobQuestionOption;
import com.muyategna.backend.job_request.entity.JobQuestionTransition;
import com.muyategna.backend.job_request.entity.JobRequestFlowQuestion;

import java.util.List;

public final class JobQuestionTransitionMapper {

    public static JobQuestionTransitionDto toDto(JobQuestionTransition jobQuestionTransition) {
        return JobQuestionTransitionDto.builder()
                .id(jobQuestionTransition.getId())
                .flowId(jobQuestionTransition.getFlow().getId())
                .fromFlowQuestionId(jobQuestionTransition.getFromFlowQuestion().getId())
                .toFlowQuestionId(jobQuestionTransition.getToFlowQuestion().getId())
                .optionId(jobQuestionTransition.getOption() == null ? null : jobQuestionTransition.getOption().getId())
                .conditionExpression(jobQuestionTransition.getConditionExpression())
                .createdAt(jobQuestionTransition.getCreatedAt())
                .updatedAt(jobQuestionTransition.getUpdatedAt())
                .build();
    }


    public static JobQuestionTransitionMinimalDto toMinimalDto(JobQuestionTransition jobQuestionTransition) {
        return JobQuestionTransitionMinimalDto.builder()
                .id(jobQuestionTransition.getId())
                .flowId(jobQuestionTransition.getFlow().getId())
                .fromFlowQuestionId(jobQuestionTransition.getFromFlowQuestion().getId())
                .toFlowQuestionId(jobQuestionTransition.getToFlowQuestion().getId())
                .optionId(jobQuestionTransition.getOption() == null ? null : jobQuestionTransition.getOption().getId())
                .conditionExpression(jobQuestionTransition.getConditionExpression())
                .build();
    }


    public static JobQuestionTransition toEntity(JobQuestionTransitionDto jobQuestionTransitionDto,
                                                 JobRequestFlowQuestion currentFlowQuestion,
                                                 JobRequestFlowQuestion nextFlowQuestion,
                                                 JobQuestionOption option) {

        if (jobQuestionTransitionDto == null) {
            return null;
        }
        JobQuestionTransition jobQuestionTransition = new JobQuestionTransition();
        jobQuestionTransition.setId(jobQuestionTransitionDto.getId());
        jobQuestionTransition.setConditionExpression(jobQuestionTransitionDto.getConditionExpression());
        jobQuestionTransition.setCreatedAt(jobQuestionTransitionDto.getCreatedAt());
        jobQuestionTransition.setUpdatedAt(jobQuestionTransitionDto.getUpdatedAt());
        jobQuestionTransition.setFlow(currentFlowQuestion.getFlow());
        jobQuestionTransition.setFromFlowQuestion(currentFlowQuestion);
        jobQuestionTransition.setToFlowQuestion(nextFlowQuestion);
        jobQuestionTransition.setOption(option);
        return jobQuestionTransition;
    }


    public static JobQuestionTransition toEntity(JobQuestionTransitionCreateDto jobQuestionTransitionDto,
                                                 JobRequestFlowQuestion currentFlowQuestion,
                                                 JobRequestFlowQuestion nextFlowQuestion,
                                                 JobQuestionOption option) {

        if (jobQuestionTransitionDto == null) {
            return null;
        }
        JobQuestionTransition jobQuestionTransition = new JobQuestionTransition();
        jobQuestionTransition.setConditionExpression(jobQuestionTransitionDto.getConditionExpression());
        jobQuestionTransition.setFlow(currentFlowQuestion.getFlow());
        jobQuestionTransition.setFromFlowQuestion(currentFlowQuestion);
        jobQuestionTransition.setToFlowQuestion(nextFlowQuestion);
        jobQuestionTransition.setOption(option);
        return jobQuestionTransition;
    }


    public static JobQuestionTransition toEntity(JobQuestionTransitionUpdateDto jobQuestionTransitionDto,
                                                 JobRequestFlowQuestion currentFlowQuestion,
                                                 JobRequestFlowQuestion nextFlowQuestion,
                                                 JobQuestionOption option) {

        if (jobQuestionTransitionDto == null) {
            return null;
        }
        JobQuestionTransition jobQuestionTransition = new JobQuestionTransition();
        jobQuestionTransition.setId(jobQuestionTransitionDto.getId());
        jobQuestionTransition.setConditionExpression(jobQuestionTransitionDto.getConditionExpression());
        jobQuestionTransition.setFlow(currentFlowQuestion.getFlow());
        jobQuestionTransition.setFromFlowQuestion(currentFlowQuestion);
        jobQuestionTransition.setToFlowQuestion(nextFlowQuestion);
        jobQuestionTransition.setOption(option);
        return jobQuestionTransition;
    }


    public static List<JobQuestionTransitionDto> toDtoList(List<JobQuestionTransition> jobQuestionTransitions) {
        return jobQuestionTransitions.stream().map(JobQuestionTransitionMapper::toDto).toList();
    }


    public static List<JobQuestionTransitionMinimalDto> toMinimalDtoList(List<JobQuestionTransition> jobQuestionTransitions) {
        return jobQuestionTransitions.stream().map(JobQuestionTransitionMapper::toMinimalDto).toList();
    }

}
