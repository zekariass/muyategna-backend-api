package com.muyategna.backend.messaging.message.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.PublicPaginationService;
import com.muyategna.backend.messaging.message.assembler.MessageListViewDtoModelPublicAssembler;
import com.muyategna.backend.messaging.message.dto.MessageCreateDto;
import com.muyategna.backend.messaging.message.dto.MessageListViewDto;
import com.muyategna.backend.messaging.message.entity.Message;
import com.muyategna.backend.messaging.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/secured/public/messages")
@Tag(name = "Message Controller", description = "Message Controller")
public class MessageSecuredPublicController {

    private final MessageService messageService;
    private final MessageListViewDtoModelPublicAssembler listViewAssembler;
    private final PagedResourcesAssembler<MessageListViewDto> pagedAssembler;
    private final PublicPaginationService publicPaginationService;

    @Autowired
    public MessageSecuredPublicController(MessageService messageService, MessageListViewDtoModelPublicAssembler listViewAssembler, PagedResourcesAssembler<MessageListViewDto> pagedAssembler, PublicPaginationService publicPaginationService) {
        this.messageService = messageService;
        this.listViewAssembler = listViewAssembler;
        this.pagedAssembler = pagedAssembler;
        this.publicPaginationService = publicPaginationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new message", description = "Create a new message in the system.")
    public ResponseEntity<ApiResponse<EntityModel<MessageListViewDto>>> createMessage(
            @Parameter(description = "Message Object") @Valid @ModelAttribute MessageCreateDto messageCreateDto,
            HttpServletRequest request
    ) {

        MessageListViewDto messageListViewDto = messageService.createMessage(messageCreateDto);
        EntityModel<MessageListViewDto> entityModel = listViewAssembler.toModel(messageListViewDto, request);
        ApiResponse<EntityModel<MessageListViewDto>> response = ApiResponse.<EntityModel<MessageListViewDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Message threads fetched successfully.")
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/thread/{threadId}")
    @Operation(summary = "Get messages by thread id", description = "Get messages by thread id")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<MessageListViewDto>>>> getMessageThreadById(
            @Parameter(description = "Message Thread id", required = true) @PathVariable Long threadId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt,desc") String sortBy,
            HttpServletRequest request
    ) {

        Pageable pageable = publicPaginationService.getPagination(page, size, sortBy, Message.class);
        // Fetch the message thread by ID

        Page<MessageListViewDto> pagedDtoList = messageService.getPagedMessagesByThreadId(threadId, pageable);

        PagedModel<EntityModel<MessageListViewDto>> entityModel = pagedAssembler.toModel(
                pagedDtoList,
                dto -> listViewAssembler.toModel(dto, request));

        ApiResponse<PagedModel<EntityModel<MessageListViewDto>>> response = ApiResponse.<PagedModel<EntityModel<MessageListViewDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Messages fetched successfully for message thread with ID: " + threadId + ".")
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }

}
