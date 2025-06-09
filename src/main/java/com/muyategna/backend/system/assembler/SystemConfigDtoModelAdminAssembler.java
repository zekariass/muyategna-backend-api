package com.muyategna.backend.system.assembler;

import com.muyategna.backend.system.controller.admin.SystemConfigAdminController;
import com.muyategna.backend.system.dto.SystemConfigDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SystemConfigDtoModelAdminAssembler {

    public EntityModel<SystemConfigDto> toModel(SystemConfigDto systemConfigDto, Integer page, Integer size, String sortBy, HttpServletRequest request) {
        return EntityModel.of(
                systemConfigDto,
                linkTo(methodOn(SystemConfigAdminController.class).getSystemConfigById(systemConfigDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(SystemConfigAdminController.class).getSystemConfigByName(systemConfigDto.getName(), request)).withSelfRel(),
                linkTo(methodOn(SystemConfigAdminController.class).getAllSystemConfigs(page, size, sortBy, request)).withRel("allSystemConfigs")
        );
    }

    public EntityModel<SystemConfigDto> toModel(SystemConfigDto systemConfigDto, HttpServletRequest request) {
        return EntityModel.of(
                systemConfigDto,
                linkTo(methodOn(SystemConfigAdminController.class).getSystemConfigById(systemConfigDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(SystemConfigAdminController.class).getSystemConfigByName(systemConfigDto.getName(), request)).withSelfRel(),
                linkTo(methodOn(SystemConfigAdminController.class).getAllSystemConfigs(1, 10, "id", request)).withRel("allSystemConfigs")
        );
    }


    public CollectionModel<EntityModel<SystemConfigDto>> toCollectionModel(Iterable<? extends SystemConfigDto> systemConfigDtoList, int page, int size, String sortBy, HttpServletRequest request) {
        List<EntityModel<SystemConfigDto>> systemConfigDtoModels = StreamSupport.stream(systemConfigDtoList.spliterator(), false)
                .map(systemConfigDto -> this.toModel(systemConfigDto, page, size, sortBy, request))
                .toList();
        return CollectionModel.of(systemConfigDtoModels,
                linkTo(methodOn(SystemConfigAdminController.class).getAllSystemConfigs(page, size, sortBy, request)).withRel("allSystemConfigs")
        );
    }
}
