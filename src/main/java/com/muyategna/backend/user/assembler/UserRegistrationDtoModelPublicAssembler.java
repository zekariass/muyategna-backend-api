package com.muyategna.backend.user.assembler;

import com.muyategna.backend.user.dto.UserRegistrationResultDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserRegistrationDtoModelPublicAssembler {

    public EntityModel<UserRegistrationResultDto> toModel(UserRegistrationResultDto userRegistrationDto, HttpServletRequest request) {
        return  EntityModel.of(userRegistrationDto);
    }

    public CollectionModel<EntityModel<UserRegistrationResultDto>> toCollectionModel(Iterable<? extends UserRegistrationResultDto> userRegistrationDtos, HttpServletRequest request) {
        return CollectionModel.of(StreamSupport.stream(userRegistrationDtos.spliterator(), false)
                .map(userRegistrationDto -> toModel(userRegistrationDto, request))
                .collect(Collectors.toList()));
    }
}
