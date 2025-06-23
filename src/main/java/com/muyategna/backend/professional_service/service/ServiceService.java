package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    Page<ServiceLocalizedDto> getAllServicesForCurrentCountry(Pageable pageable);

    ServiceLocalizedDto getServiceByIdForCurrentCountry(Long serviceId);

    Optional<Service> getServiceById(Long serviceId);

    List<ServiceLocalizedDto> getServicesByNameForCurrentCountry(String serviceName);

    Page<ServiceLocalizedDto> getPagedServicesByNameForCurrentCountry(String serviceName, Pageable pageable);

    List<ServiceLocalizedDto> getServicesByCategoryForCurrentCountry(Long categoryId);

    Page<ServiceLocalizedDto> getPagedServicesByCategoryForCurrentCountry(Long categoryId, Pageable pageable);

}
