package com.muyategna.backend.professional_service.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/professional-service/services")
@Tag(name = "Service Admin Management", description = "Service Admin Controller")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class ServiceAdminController {
    // TODO: Implement the methods for managing services in the admin panel
}
