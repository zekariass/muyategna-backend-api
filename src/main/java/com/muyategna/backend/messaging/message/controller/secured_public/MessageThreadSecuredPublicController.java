package com.muyategna.backend.messaging.message.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.PublicPaginationService;
import com.muyategna.backend.messaging.message.assembler.MessageThreadDetailViewDtoModelPublicAssembler;
import com.muyategna.backend.messaging.message.assembler.MessageThreadListViewDtoModelPublicAssembler;
import com.muyategna.backend.messaging.message.dto.MessageThreadCreateDto;
import com.muyategna.backend.messaging.message.dto.MessageThreadDetailViewDto;
import com.muyategna.backend.messaging.message.dto.MessageThreadListViewDto;
import com.muyategna.backend.messaging.message.entity.MessageThread;
import com.muyategna.backend.messaging.message.service.MessageThreadService;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/secured/public/message-threads")
@Tag(name = "Message Thread Controller", description = "Secured Thread Controller")
public class MessageThreadSecuredPublicController {

    private final MessageThreadService messageThreadService;
    private final MessageThreadListViewDtoModelPublicAssembler listModelAssembler;
    private final MessageThreadDetailViewDtoModelPublicAssembler detailModelAssembler;
    private final PagedResourcesAssembler<MessageThreadListViewDto> pagedResourcesAssembler;
    private final PublicPaginationService publicPaginationService;

    @Autowired
    public MessageThreadSecuredPublicController(MessageThreadService messageThreadService,
                                                MessageThreadListViewDtoModelPublicAssembler listModelAssembler,
                                                MessageThreadDetailViewDtoModelPublicAssembler detailModelAssembler,
                                                PagedResourcesAssembler<MessageThreadListViewDto> pagedResourcesAssembler,
                                                PublicPaginationService publicPaginationService) {
        this.messageThreadService = messageThreadService;
        this.listModelAssembler = listModelAssembler;
        this.detailModelAssembler = detailModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.publicPaginationService = publicPaginationService;
    }

    @Operation(summary = "Create a new message thread", description = "Create a new message thread.")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<EntityModel<MessageThreadListViewDto>>> createMessageThread(
            @Parameter(description = "Message Thread Create DTO") @Valid @ModelAttribute MessageThreadCreateDto messageThreadCreateDto,
            HttpServletRequest request
    ) {

        MessageThreadListViewDto messageThread = messageThreadService.createMessageThread(messageThreadCreateDto);

        EntityModel<MessageThreadListViewDto> entityModel = listModelAssembler.toModel(messageThread, request);
        ApiResponse<EntityModel<MessageThreadListViewDto>> response = ApiResponse.<EntityModel<MessageThreadListViewDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Message thread created successfully.")
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .data(entityModel)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{messageThreadId}")
    @Operation(summary = "Get message thread by ID", description = "Get a message thread by its ID.")
    public ResponseEntity<ApiResponse<EntityModel<MessageThreadDetailViewDto>>> getMessageThreadDetailById(
            @Parameter(description = "Message Thread ID", required = true) @PathVariable Long messageThreadId,
            HttpServletRequest request
    ) {

        MessageThreadDetailViewDto messageThread = messageThreadService.getMessageThreadDetailById(messageThreadId);

        EntityModel<MessageThreadDetailViewDto> entityModel = detailModelAssembler.toModel(messageThread, request);

        ApiResponse<EntityModel<MessageThreadDetailViewDto>> response = ApiResponse.<EntityModel<MessageThreadDetailViewDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Message thread fetched successfully.")
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get all message threads of a service provider", description = "Get all message threads of a service provider.")
    @GetMapping("/service-provider/{serviceProviderId}")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<MessageThreadListViewDto>>>> getAllMessageThreadsForServiceProvider(
            @Parameter(description = "Service Provider ID", required = true) @PathVariable Long serviceProviderId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt,desc") String sortBy,
            HttpServletRequest request) {

        Pageable pageable = publicPaginationService.getPagination(page, size, sortBy, MessageThread.class);

        Page<MessageThreadListViewDto> pagedThreads = messageThreadService.getAllMessageThreadsForServiceProvider(serviceProviderId, pageable);

        PagedModel<EntityModel<MessageThreadListViewDto>> pagedResponse = pagedResourcesAssembler.toModel(
                pagedThreads, thread -> listModelAssembler.toModel(thread, request));

        ApiResponse<PagedModel<EntityModel<MessageThreadListViewDto>>> response = ApiResponse.<PagedModel<EntityModel<MessageThreadListViewDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Message threads fetched successfully.")
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .data(pagedResponse)
                .build();
        return ResponseEntity.ok(response);

    }


    @Operation(summary = "Get all message threads of a customer", description = "Get all message threads of a customer.")
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<MessageThreadListViewDto>>>> getAllMessageThreadsForCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt,desc") String sortBy,
            HttpServletRequest request) {

        Pageable pageable = publicPaginationService.getPagination(page, size, sortBy, MessageThread.class);

        Page<MessageThreadListViewDto> pagedThreads = messageThreadService.getAllMessageThreadsForCustomer(customerId, pageable);

        PagedModel<EntityModel<MessageThreadListViewDto>> pagedResponse = pagedResourcesAssembler.toModel(
                pagedThreads, thread -> listModelAssembler.toModel(thread, request));

        ApiResponse<PagedModel<EntityModel<MessageThreadListViewDto>>> response = ApiResponse.<PagedModel<EntityModel<MessageThreadListViewDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Message threads fetched successfully.")
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .data(pagedResponse)
                .build();
        return ResponseEntity.ok(response);

    }
}
