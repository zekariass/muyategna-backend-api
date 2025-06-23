package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowPublicDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class JobRequestFlowPublicDtoModelAssembler {
    public EntityModel<JobRequestFlowPublicDto> toModel(JobRequestFlowPublicDto jobRequestFlowPublicDto,
                                                        HttpServletRequest request) {
        return EntityModel.of(jobRequestFlowPublicDto);
    }
}
