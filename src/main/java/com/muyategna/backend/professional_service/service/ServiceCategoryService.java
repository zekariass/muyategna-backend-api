package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryLocalizedDto;

import java.util.List;

public interface ServiceCategoryService {
    ServiceCategoryLocalizedDto getLocalizedServiceCategoryById(Long categoryId);

    List<ServiceCategoryLocalizedDto> getAllLocalizedServiceCategories();
}
