package com.muyategna.backend.messaging.message.assembler;

import com.muyategna.backend.messaging.message.dto.MessageThreadListViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class MessageThreadListViewDtoModelPublicAssembler {

    public EntityModel<MessageThreadListViewDto> toModel(MessageThreadListViewDto messageThreadListViewDto, HttpServletRequest request) {
        return EntityModel.of(messageThreadListViewDto);
    }


    public CollectionModel<EntityModel<MessageThreadListViewDto>> toCollectionModel(Iterable<? extends MessageThreadListViewDto> messageThreadsDto, HttpServletRequest request) {
        List<EntityModel<MessageThreadListViewDto>> messageThreadsModel = StreamSupport.stream(messageThreadsDto.spliterator(), false)
                .map(messageThreadDto -> this.toModel(messageThreadDto, request))
                .toList();
        return CollectionModel.of(messageThreadsModel);
    }
}
