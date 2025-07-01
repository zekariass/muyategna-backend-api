package com.muyategna.backend.billing.controller.secured_public;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secured/public/billing/service-provider/{serviceProviderId}/add-ons")
@Tag(name = "Service Provider Add On", description = "Service Provider Add On")
@PreAuthorize("isAuthenticated()")
public class ServiceProviderAddOnSecuredPublicController {
}
