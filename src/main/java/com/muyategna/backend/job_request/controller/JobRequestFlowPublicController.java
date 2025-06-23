package com.muyategna.backend.job_request.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.job_request.assembler.JobRequestFlowPublicDtoModelAssembler;
import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowPublicDto;
import com.muyategna.backend.job_request.service.JobRequestFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/job-request/flow")
@Tag(name = "Job Request Flow", description = "Job Request Flow")
public class JobRequestFlowPublicController {

    private final JobRequestFlowService jobRequestFlowService;
    private final JobRequestFlowPublicDtoModelAssembler assembler;

    @Autowired
    public JobRequestFlowPublicController(JobRequestFlowService jobRequestFlowService,
                                          JobRequestFlowPublicDtoModelAssembler assembler) {
        this.jobRequestFlowService = jobRequestFlowService;
        this.assembler = assembler;
    }


    @GetMapping("/{flowId}")
    @Operation(summary = "Get job request flow by id")
    public ResponseEntity<ApiResponse<EntityModel<JobRequestFlowPublicDto>>> getJobRequestFlowById(@Parameter(description = "Job request flow id") @PathVariable Long flowId,
                                                                                                   HttpServletRequest request) {
        JobRequestFlowPublicDto jobRequestFlow = jobRequestFlowService.getFlowByIdForPublic(flowId);
        EntityModel<JobRequestFlowPublicDto> model = assembler.toModel(jobRequestFlow, request);
        ApiResponse<EntityModel<JobRequestFlowPublicDto>> response = ApiResponse.<EntityModel<JobRequestFlowPublicDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(jobRequestFlow == null ? "Job request flow not found" : "Job request flow fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(model)
                .build();
        return ResponseEntity.ok(response);
    }

}
