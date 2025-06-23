package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_question_answer.JobQuestionAnswerCreateDto;
import com.muyategna.backend.job_request.dto.job_question_answer.JobQuestionAnswerDto;
import com.muyategna.backend.job_request.dto.job_question_answer.JobQuestionAnswerUpdateDto;
import com.muyategna.backend.job_request.entity.JobQuestionAnswer;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.entity.JobRequestFlowQuestion;

import java.util.List;

public final class JobQuestionAnswerMapper {

    public static JobQuestionAnswerDto toDto(JobQuestionAnswer jobQuestionAnswer) {
        if (jobQuestionAnswer == null) {
            return null;
        }
        return JobQuestionAnswerDto.builder()
                .id(jobQuestionAnswer.getId())
                .jobRequestId(jobQuestionAnswer.getJobRequest().getId())
                .flowQuestionId(jobQuestionAnswer.getFlowQuestion().getId())
                .answerText(jobQuestionAnswer.getAnswerText())
                .answerNumber(jobQuestionAnswer.getAnswerNumber())
                .answerBoolean(jobQuestionAnswer.getAnswerBoolean())
                .answerDate(jobQuestionAnswer.getAnswerDate())
                .answerOptionIds(jobQuestionAnswer.getAnswerOptionIds())
                .createdAt(jobQuestionAnswer.getCreatedAt())
                .updatedAt(jobQuestionAnswer.getUpdatedAt())
                .build();
    }


    public static JobQuestionAnswer toEntity(JobQuestionAnswerDto jobQuestionAnswerDto,
                                             JobRequest jobRequest,
                                             JobRequestFlowQuestion flowQuestion
    ) {
        if (jobQuestionAnswerDto == null) {
            return null;
        }
        JobQuestionAnswer jobQuestionAnswer = new JobQuestionAnswer();
        jobQuestionAnswer.setId(jobQuestionAnswerDto.getId());
        jobQuestionAnswer.setJobRequest(jobRequest);
        jobQuestionAnswer.setFlowQuestion(flowQuestion);
        jobQuestionAnswer.setAnswerText(jobQuestionAnswerDto.getAnswerText());
        jobQuestionAnswer.setAnswerNumber(jobQuestionAnswerDto.getAnswerNumber());
        jobQuestionAnswer.setAnswerBoolean(jobQuestionAnswerDto.getAnswerBoolean());
        jobQuestionAnswer.setAnswerDate(jobQuestionAnswerDto.getAnswerDate());
        jobQuestionAnswer.setAnswerOptionIds(jobQuestionAnswerDto.getAnswerOptionIds());
        jobQuestionAnswer.setCreatedAt(jobQuestionAnswerDto.getCreatedAt());
        return jobQuestionAnswer;
    }


    public static JobQuestionAnswer toEntity(JobQuestionAnswerCreateDto jobQuestionAnswerDto,
                                             JobRequest jobRequest,
                                             JobRequestFlowQuestion flowQuestion) {
        if (jobQuestionAnswerDto == null) {
            return null;
        }
        JobQuestionAnswer jobQuestionAnswer = new JobQuestionAnswer();
        jobQuestionAnswer.setJobRequest(jobRequest);
        jobQuestionAnswer.setFlowQuestion(flowQuestion);
        jobQuestionAnswer.setAnswerText(jobQuestionAnswerDto.getAnswerText());
        jobQuestionAnswer.setAnswerNumber(jobQuestionAnswerDto.getAnswerNumber());
        jobQuestionAnswer.setAnswerBoolean(jobQuestionAnswerDto.getAnswerBoolean());
        jobQuestionAnswer.setAnswerDate(jobQuestionAnswerDto.getAnswerDate());
        jobQuestionAnswer.setAnswerOptionIds(jobQuestionAnswerDto.getAnswerOptionIds());

        return jobQuestionAnswer;
    }


    public static JobQuestionAnswer toEntity(JobQuestionAnswerUpdateDto jobQuestionAnswerDto,
                                             JobRequest jobRequest,
                                             JobRequestFlowQuestion flowQuestion) {
        if (jobQuestionAnswerDto == null) {
            return null;
        }
        JobQuestionAnswer jobQuestionAnswer = new JobQuestionAnswer();
        jobQuestionAnswer.setId(jobQuestionAnswerDto.getId());
        jobQuestionAnswer.setJobRequest(jobRequest);
        jobQuestionAnswer.setFlowQuestion(flowQuestion);
        jobQuestionAnswer.setAnswerText(jobQuestionAnswerDto.getAnswerText());
        jobQuestionAnswer.setAnswerNumber(jobQuestionAnswerDto.getAnswerNumber());
        jobQuestionAnswer.setAnswerBoolean(jobQuestionAnswerDto.getAnswerBoolean());
        jobQuestionAnswer.setAnswerDate(jobQuestionAnswerDto.getAnswerDate());
        jobQuestionAnswer.setAnswerOptionIds(jobQuestionAnswerDto.getAnswerOptionIds());
        return jobQuestionAnswer;
    }


    public static List<JobQuestionAnswerDto> toDtoList(List<JobQuestionAnswer> jobQuestionAnswers) {
        return jobQuestionAnswers.stream().map(JobQuestionAnswerMapper::toDto).toList();
    }
}
