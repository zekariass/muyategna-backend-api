package com.muyategna.backend.messaging.message.assembler;

import com.muyategna.backend.messaging.message.dto.MessageListViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class MessageListViewDtoModelPublicAssembler {

    public EntityModel<MessageListViewDto> toModel(MessageListViewDto messageListViewDto, HttpServletRequest request) {
        return EntityModel.of(messageListViewDto);
    }


    public CollectionModel<EntityModel<MessageListViewDto>> toCollectionModel(Iterable<? extends MessageListViewDto> messageListViewDtos, HttpServletRequest request) {
        List<EntityModel<MessageListViewDto>> messageListViewDtoModels = StreamSupport.stream(messageListViewDtos.spliterator(), false)
                .map(messageListViewDto -> toModel(messageListViewDto, request))
                .toList();
        return CollectionModel.of(messageListViewDtoModels);
    }
}
