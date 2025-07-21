package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.request_media.JobRequestMediaDto;
import com.muyategna.backend.job_request.dto.request_media.JobRequestMediaMinimalDto;
import com.muyategna.backend.job_request.entity.JobRequestMedia;

import java.util.List;

public final class JobRequestMediaMapper {

    public static JobRequestMediaDto toDto(JobRequestMedia jobRequestMedia) {
        return JobRequestMediaDto.builder()
                .id(jobRequestMedia.getId())
                .request(jobRequestMedia.getRequest())
                .mediaUrl(jobRequestMedia.getMediaUrl())
                .build();
    }


    public static JobRequestMediaMinimalDto toMinimalDto(JobRequestMedia jobRequestMedia) {
        return JobRequestMediaMinimalDto.builder()
                .id(jobRequestMedia.getId())
                .requestId(jobRequestMedia.getRequest().getId())
                .mediaUrl(jobRequestMedia.getMediaUrl())
                .build();
    }


    public static List<JobRequestMediaDto> toDtoList(List<JobRequestMedia> jobRequestMediaList) {
        return jobRequestMediaList.stream().map(JobRequestMediaMapper::toDto).toList();
    }


    public static List<JobRequestMediaMinimalDto> toMinimalDtoList(List<JobRequestMedia> jobRequestMediaList) {
        return jobRequestMediaList.stream().map(JobRequestMediaMapper::toMinimalDto).toList();
    }
}
