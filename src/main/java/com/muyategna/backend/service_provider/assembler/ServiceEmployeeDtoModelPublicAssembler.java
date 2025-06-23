package com.muyategna.backend.service_provider.assembler;

import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ServiceEmployeeDtoModelPublicAssembler {
    public EntityModel<ServiceEmployeeDto> toModel(ServiceEmployeeDto serviceEmployeeDto, HttpServletRequest request) {
        return EntityModel.of(serviceEmployeeDto);
    }


    public CollectionModel<EntityModel<ServiceEmployeeDto>> toCollectionModel(Iterable<? extends ServiceEmployeeDto> serviceEmployeeDtos, HttpServletRequest request) {
        List<EntityModel<ServiceEmployeeDto>> serviceEmployeeDtoModels = StreamSupport
                .stream(serviceEmployeeDtos.spliterator(), false)
                .map(serviceEmployeeDto -> toModel(serviceEmployeeDto, request))
                .toList();
        return CollectionModel.of(serviceEmployeeDtoModels);
    }
}
