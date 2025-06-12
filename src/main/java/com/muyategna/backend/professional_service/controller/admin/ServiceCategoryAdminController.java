package com.muyategna.backend.professional_service.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/professional-service/service-categories")
@Tag(name = "Service Category Admin Management", description = "Service Category Admin Controller")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class ServiceCategoryAdminController {
    // TODO: Implement the methods for managing service categories in the admin panel
}
