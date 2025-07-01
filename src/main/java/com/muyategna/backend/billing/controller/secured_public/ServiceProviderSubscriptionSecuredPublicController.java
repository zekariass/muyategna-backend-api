package com.muyategna.backend.billing.controller.secured_public;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secured/public/billing/service-provider/{serviceProviderId}/subscription")
@Tag(name = "Service Provider Subscription", description = "Service Provider Subscription")
@PreAuthorize("isAuthenticated()")
public class ServiceProviderSubscriptionSecuredPublicController {
}
