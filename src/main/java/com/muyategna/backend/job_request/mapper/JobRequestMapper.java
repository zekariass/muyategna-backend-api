package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_request.JobRequestCreateDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestMinimalDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestUpdateDto;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.user.entity.UserProfile;

import java.util.List;

public final class JobRequestMapper {

    public static JobRequestMinimalDto toMinimalDto(JobRequest entity) {
        if (entity == null) {
            return null;
        }
        return JobRequestMinimalDto.builder()
                .id(entity.getId())
                .serviceId(entity.getService().getId())
                .customerId(entity.getCustomer().getId())
                .description(entity.getDescription())
                .budget(entity.getBudget())
                .whenToStartJob(entity.getWhenToStartJob())
                .status(entity.getStatus())
                .addressId(entity.getAddress().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static JobRequestDto toDto(JobRequest entity) {
        if (entity == null) {
            return null;
        }
        return JobRequestDto.builder()
                .id(entity.getId())
                .serviceId(entity.getService().getId())
                .customerId(entity.getCustomer().getId())
                .description(entity.getDescription())
                .budget(entity.getBudget())
                .whenToStartJob(entity.getWhenToStartJob())
                .status(entity.getStatus())
                .addressId(entity.getAddress().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static JobRequest toEntity(JobRequestDto dto,
                                      Service service,
                                      UserProfile customer,
                                      Address address) {
        if (dto == null) {
            return null;
        }
        JobRequest entity = new JobRequest();
        entity.setId(dto.getId());
        entity.setService(service);
        entity.setCustomer(customer);
        entity.setDescription(dto.getDescription());
        entity.setBudget(dto.getBudget());
        entity.setWhenToStartJob(dto.getWhenToStartJob());
        entity.setStatus(dto.getStatus());
        entity.setAddress(address);
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }


    public static JobRequest toEntity(JobRequestCreateDto dto,
                                      Service service,
                                      UserProfile customer,
                                      Address address) {
        if (dto == null) {
            return null;
        }
        JobRequest entity = new JobRequest();
        entity.setService(service);
        entity.setCustomer(customer);
        entity.setDescription(dto.getDescription());
        entity.setBudget(dto.getBudget());
        entity.setWhenToStartJob(dto.getWhenToStartJob());
        entity.setStatus(dto.getStatus());
        entity.setAddress(address);
        return entity;
    }

    public static JobRequest toEntity(JobRequestUpdateDto dto,
                                      Service service,
                                      UserProfile customer,
                                      Address address) {
        if (dto == null) {
            return null;
        }
        JobRequest entity = new JobRequest();
        entity.setId(dto.getId());
        entity.setService(service);
        entity.setCustomer(customer);
        entity.setDescription(dto.getDescription());
        entity.setBudget(dto.getBudget());
        entity.setWhenToStartJob(dto.getWhenToStartJob());
        entity.setStatus(dto.getStatus());
        entity.setAddress(address);
        return entity;
    }

    public static List<JobRequestMinimalDto> toMinimalDtoList(List<JobRequest> jobRequests) {
        return jobRequests.stream().map(JobRequestMapper::toMinimalDto).toList();
    }
}
