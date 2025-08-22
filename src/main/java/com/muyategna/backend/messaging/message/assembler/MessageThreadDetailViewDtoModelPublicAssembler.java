package com.muyategna.backend.messaging.message.assembler;

import com.muyategna.backend.messaging.message.dto.MessageThreadDetailViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class MessageThreadDetailViewDtoModelPublicAssembler {

    public EntityModel<MessageThreadDetailViewDto> toModel(MessageThreadDetailViewDto messageThreadDetailViewDto, HttpServletRequest request) {
        return EntityModel.of(messageThreadDetailViewDto);
    }

    public CollectionModel<EntityModel<MessageThreadDetailViewDto>> toCollectionModel(Iterable<? extends MessageThreadDetailViewDto> messageThreadDetailViewDtoList, HttpServletRequest request) {
        List<EntityModel<MessageThreadDetailViewDto>> messageThreadDetailViewDtoModels = StreamSupport
                .stream(messageThreadDetailViewDtoList.spliterator(), false)
                .map(messageThreadDetailViewDto -> toModel(messageThreadDetailViewDto, request))
                .toList();
        return CollectionModel.of(messageThreadDetailViewDtoModels);
    }
}
