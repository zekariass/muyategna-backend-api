package com.muyategna.backend.job_request.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.job_request.assembler.JobRequestCreateDtoModelAssembler;
import com.muyategna.backend.job_request.dto.job_request.JobRequestCreateDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/job-requests")
@Tag(name = "Job Request Public Controller", description = "Job Request Public API")
public class JobRequestPublicController {

    private final JobRequestService jobRequestService;
    private final JobRequestCreateDtoModelAssembler assembler;

    @Autowired
    public JobRequestPublicController(JobRequestService jobRequestService, JobRequestCreateDtoModelAssembler assembler) {
        this.jobRequestService = jobRequestService;
        this.assembler = assembler;
    }


    @PostMapping
    @Operation(summary = "Create Job Request", description = "Create Job Request")
    public ResponseEntity<ApiResponse<EntityModel<JobRequestDto>>> createJobRequest(@Parameter(description = "Job Request Create DTO") @Valid @RequestBody JobRequestCreateDto jobRequestCreateDto,
                                                                                    HttpServletRequest request) {

        JobRequestDto jobRequestDto = jobRequestService.createJobRequest(jobRequestCreateDto);
        EntityModel<JobRequestDto> entityModel = assembler.toModel(jobRequestDto, request);
        ApiResponse<EntityModel<JobRequestDto>> response = ApiResponse.<EntityModel<JobRequestDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Job request created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }
}
