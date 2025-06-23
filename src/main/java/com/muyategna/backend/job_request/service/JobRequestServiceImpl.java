package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_question_answer.JobQuestionAnswerCreateDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestCreateDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import com.muyategna.backend.job_request.entity.JobQuestionAnswer;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.entity.JobRequestFlowQuestion;
import com.muyategna.backend.job_request.mapper.JobQuestionAnswerMapper;
import com.muyategna.backend.job_request.mapper.JobRequestMapper;
import com.muyategna.backend.job_request.repository.JobQuestionAnswerRepository;
import com.muyategna.backend.job_request.repository.JobRequestFlowQuestionRepository;
import com.muyategna.backend.job_request.repository.JobRequestRepository;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.location.service.AddressService;
import com.muyategna.backend.professional_service.repository.ServiceRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobRequestServiceImpl implements JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final ServiceRepository serviceRepository;
    private final UserProfileRepository userProfileRepository;
    private final AddressService addressService;
    private final JobRequestFlowQuestionRepository jobRequestFlowQuestionRepository;
    private final JobQuestionAnswerRepository jobQuestionAnswerRepository;

    @Autowired
    public JobRequestServiceImpl(JobRequestRepository jobRequestRepository,
                                 ServiceRepository serviceRepository,
                                 UserProfileRepository userProfileRepository,
                                 AddressService addressService,
                                 JobRequestFlowQuestionRepository jobRequestFlowQuestionRepository, JobQuestionAnswerRepository jobQuestionAnswerRepository) {
        this.jobRequestRepository = jobRequestRepository;
        this.serviceRepository = serviceRepository;
        this.userProfileRepository = userProfileRepository;
        this.addressService = addressService;
        this.jobRequestFlowQuestionRepository = jobRequestFlowQuestionRepository;
        this.jobQuestionAnswerRepository = jobQuestionAnswerRepository;
    }

    /**
     * Creates a new job request based on the provided JobRequestCreateDto.
     *
     * @param jobRequestCreateDto the DTO containing the details of the job request to be created
     * @return the created JobRequestDto
     */
    @Override
    public JobRequestDto createJobRequest(JobRequestCreateDto jobRequestCreateDto) {
        com.muyategna.backend.professional_service.entity.Service service = serviceRepository
                .findById(jobRequestCreateDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + jobRequestCreateDto.getServiceId()));
        UserProfile customer = userProfileRepository
                .findById(jobRequestCreateDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + jobRequestCreateDto.getCustomerId()));

        Address address = addressService.createAddress(jobRequestCreateDto.getAddressCreateDto());

        List<JobQuestionAnswerCreateDto> answers = jobRequestCreateDto.getAnswers();

        JobRequest jobRequest = JobRequestMapper.toEntity(jobRequestCreateDto, service, customer, address);
        List<JobQuestionAnswer> jobQuestionAnswers = new ArrayList<>();

        JobRequest savedJobRequest = jobRequestRepository.save(jobRequest);

        for (JobQuestionAnswerCreateDto answer : jobRequestCreateDto.getAnswers()) {
            JobRequestFlowQuestion flowQuestion = jobRequestFlowQuestionRepository
                    .findById(answer.getFlowQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Flow Question not found with ID: " + answer.getFlowQuestionId()));

            JobQuestionAnswer entity = JobQuestionAnswerMapper.toEntity(answer, savedJobRequest, flowQuestion);

            jobQuestionAnswerRepository.save(entity);
        }

        return JobRequestMapper.toDto(savedJobRequest);
    }
}
