package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class JobRequestCreateDtoModelAssembler {
    public EntityModel<JobRequestDto> toModel(JobRequestDto jobRequestDto,
                                              HttpServletRequest request) {
        return EntityModel.of(jobRequestDto);
    }
}
