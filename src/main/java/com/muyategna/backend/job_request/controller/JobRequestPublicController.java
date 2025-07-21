package com.muyategna.backend.job_request.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.job_request.assembler.JobRequestDtoModelAssembler;
import com.muyategna.backend.job_request.dto.job_request.JobRequestByNewUserWithJsonStringCreateDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import com.muyategna.backend.job_request.service.JobRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/job-requests")
@Tag(name = "Job Request Public Controller", description = "Job Request Public API")
public class JobRequestPublicController {

    private final JobRequestService jobRequestService;
    private final JobRequestDtoModelAssembler assembler;

    @Autowired
    public JobRequestPublicController(JobRequestService jobRequestService,
                                      JobRequestDtoModelAssembler assembler) {
        this.jobRequestService = jobRequestService;
        this.assembler = assembler;
    }


    /**
     * Creates a new job request for a new customer.
     * @param jsonStringDto The job request create DTO.
     * @param request The HTTP request.
     * @return The created job request DTO.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create Job Request", description = "Create Job Request")
    public ResponseEntity<ApiResponse<EntityModel<JobRequestDto>>> createJobRequest(
            @Parameter(description = "Job Request Create DTO") @Valid @ModelAttribute JobRequestByNewUserWithJsonStringCreateDto jsonStringDto,
            HttpServletRequest request) {

        JobRequestDto jobRequestDto = jobRequestService.createJobRequestForNewUser(jsonStringDto);
        EntityModel<JobRequestDto> entityModel = assembler.toModel(jobRequestDto, request);
        ApiResponse<EntityModel<JobRequestDto>> response = ApiResponse.<EntityModel<JobRequestDto>>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Job request created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }
}
