package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.professional_service.dto.service.ServiceDto;
import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;

import java.util.List;

public interface ServiceTranslationService {

    List<ServiceLocalizedDto> getServiceLocalizedDtoList(List<ServiceDto> serviceDtoList);

    ServiceTranslation getTranslationByServiceId(Long id);
}
