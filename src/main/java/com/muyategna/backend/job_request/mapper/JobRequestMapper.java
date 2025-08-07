package com.muyategna.backend.job_request.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyategna.backend.job_request.dto.job_request.*;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.entity.JobStartTimeWindow;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.location.dto.address.AddressCreateDto;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.location.mapper.AddressMapper;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.mapper.ServiceMapper;
import com.muyategna.backend.user.entity.UserProfile;

public final class JobRequestMapper {

    public static JobRequestMinimalDto toMinimalDto(JobRequest entity, ServiceTranslation serviceTranslation) {
        if (entity == null) {
            return null;
        }
        return JobRequestMinimalDto.builder()
                .id(entity.getId())
                .service(ServiceMapper.toMinimalLocalizedDto(entity.getService(), serviceTranslation))
                .customerId(entity.getCustomer().getId())
                .description(entity.getDescription())
                .budget(entity.getBudget())
                .startTimeWindow(JobStartTimeWindowMapper.toDto(entity.getStartTimeWindow()))
                .status(entity.getStatus())
                .address(AddressMapper.toFlatDto(entity.getAddress()))
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
                .startTimeWindow(JobStartTimeWindowMapper.toDto(entity.getStartTimeWindow()))
                .status(entity.getStatus())
                .addressId(entity.getAddress().getId())
                .questionAnswers(entity.getQuestionAnswers())
                .extraData(entity.getExtraData())
                .deliveryMode(entity.getDeliveryMode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static JobRequestCreateDto toCreateDto(JobRequestByNewUserWithJsonStringCreateDto jsonStringDto) {
        if (jsonStringDto == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Deserialize the JSON strings
        AddressCreateDto addressCreateDto;
//        List<JobQuestionAnswerCreateDto> answers;

        try {
            addressCreateDto = objectMapper.readValue(jsonStringDto.getAddress(), AddressCreateDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid address JSON", e);
        }
        return JobRequestCreateDto.builder()
                .serviceId(jsonStringDto.getServiceId())
                .description(jsonStringDto.getDescription())
                .budget(jsonStringDto.getBudget())
                .startTimeWindowId(jsonStringDto.getStartTimeWindowId())
                .status(JobStatus.OPEN)
                .addressCreateDto(addressCreateDto)
                .questionAnswers(jsonStringDto.parseAndGetQuestionAnswers())
                .extraData(jsonStringDto.parseAndGetExtraData())
                .files(jsonStringDto.getFiles())
                .deliveryMode(jsonStringDto.getDeliveryMode())
                .build();
    }

    public static JobRequestCreateDto toCreateDto(
            JobRequestByExistingUserWithJsonStringCreateDto jsonStringDto,
            UserProfile customer) {
        if (jsonStringDto == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // Deserialize the JSON strings
        AddressCreateDto addressCreateDto;
//        List<JobQuestionAnswerCreateDto> answers;

        try {
            addressCreateDto = objectMapper.readValue(jsonStringDto.getAddress(), AddressCreateDto.class);
//            answers = objectMapper.readValue(
//                    jsonStringDto.getAnswers(),
//                    new TypeReference<List<JobQuestionAnswerCreateDto>>() {}
//            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid address JSON", e);
        }

        return JobRequestCreateDto.builder()
                .serviceId(jsonStringDto.getServiceId())
                .description(jsonStringDto.getDescription())
                .budget(jsonStringDto.getBudget())
                .startTimeWindowId(jsonStringDto.getStartTimeWindowId())
                .status(JobStatus.OPEN)
                .addressCreateDto(addressCreateDto)
                .questionAnswers(jsonStringDto.parseAndGetQuestionAnswers())
                .extraData(jsonStringDto.parseAndGetExtraData())
                .customerId(customer.getId())
                .files(jsonStringDto.getFiles())
                .deliveryMode(jsonStringDto.getDeliveryMode())
                .build();
    }


    public static JobRequestListDto toListDto(JobRequest entity,
                                              ServiceTranslation serviceTranslation,
                                              JobStartTimeWindow startTimeWindow) {
        if (entity == null) {
            return null;
        }
        return JobRequestListDto.builder()
                .id(entity.getId())
                .service(ServiceMapper.toMinimalLocalizedDto(entity.getService(), serviceTranslation))
                .description(entity.getDescription())
                .budget(entity.getBudget())
                .startTimeWindow(JobStartTimeWindowMapper.toDto(entity.getStartTimeWindow()))
                .status(entity.getStatus())
                .address(AddressMapper.toFlatDto(entity.getAddress()))
                .deliveryMode(entity.getDeliveryMode())
                .build();
    }


    public static JobRequestDetailDto toDetailDto(JobRequest entity,
                                                  ServiceTranslation serviceTranslation) {
        if (entity == null) {
            return null;
        }
        return JobRequestDetailDto.builder()
                .id(entity.getId())
                .localizedService(ServiceMapper.toMinimalLocalizedDto(entity.getService(), serviceTranslation))
                .customerId(entity.getCustomer().getId())
                .description(entity.getDescription())
                .budget(entity.getBudget())
                .startTimeWindow(JobStartTimeWindowMapper.toDto(entity.getStartTimeWindow()))
                .status(entity.getStatus())
                .address(AddressMapper.toFlatDto(entity.getAddress()))
                .questionAnswers(entity.getQuestionAnswers())
                .extraData(entity.getExtraData())
                .mediaList(JobRequestMediaMapper.toMinimalDtoList(entity.getMediaList()))
                .deliveryMode(entity.getDeliveryMode())
                .createdAt(entity.getCreatedAt())
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
        entity.setStartTimeWindow(JobStartTimeWindowMapper.toEntity(dto.getStartTimeWindow()));
        entity.setStatus(dto.getStatus());
        entity.setAddress(address);
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setQuestionAnswers(dto.getQuestionAnswers());
        entity.setExtraData(dto.getExtraData());
        entity.setDeliveryMode(dto.getDeliveryMode());
        return entity;
    }


    public static JobRequest toEntity(JobRequestCreateDto dto,
                                      Service service,
                                      UserProfile customer,
                                      Address address,
                                      JobStartTimeWindow startTimeWindow) {
        if (dto == null) {
            return null;
        }
        JobRequest entity = new JobRequest();
        entity.setService(service);
        entity.setCustomer(customer);
        entity.setDescription(dto.getDescription());
        entity.setBudget(dto.getBudget());
        entity.setStartTimeWindow(startTimeWindow);
        entity.setStatus(dto.getStatus());
        entity.setAddress(address);
        entity.setQuestionAnswers(dto.getQuestionAnswers());
        entity.setExtraData(dto.getExtraData());
        entity.setDeliveryMode(dto.getDeliveryMode());
        return entity;
    }


    public static JobRequest toEntity(JobRequest entity, JobRequestUpdateDto jsonStringDto) {
        if (jsonStringDto == null) {
            return null;
        }

        Service service = new Service();
        service.setId(jsonStringDto.getServiceId());

        JobStartTimeWindow startTimeWindow = new JobStartTimeWindow();

        startTimeWindow.setId(jsonStringDto.getStartTimeWindowId());
        entity.setId(jsonStringDto.getId());
        entity.setService(service);
        entity.setDescription(jsonStringDto.getDescription());
        entity.setBudget(jsonStringDto.getBudget());
        entity.setStartTimeWindow(startTimeWindow);
        entity.setQuestionAnswers(jsonStringDto.getQuestionAnswers());
        entity.setExtraData(jsonStringDto.getExtraData());
        entity.setDeliveryMode(jsonStringDto.getDeliveryMode());

        return entity;
    }
}
