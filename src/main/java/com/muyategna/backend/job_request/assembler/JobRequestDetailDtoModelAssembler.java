package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_request.JobRequestDetailDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class JobRequestDetailDtoModelAssembler {
    public EntityModel<JobRequestDetailDto> toModel(JobRequestDetailDto jobRequestDetailDto,
                                                    HttpServletRequest request) {
        return EntityModel.of(jobRequestDetailDto);
    }
}
