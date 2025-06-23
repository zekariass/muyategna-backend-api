package com.muyategna.backend.service_provider.aop;

import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import com.muyategna.backend.service_provider.repository.ServiceEmployeeRoleRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.AuthUtils;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.repository.UserProfileRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Aspect
@Component
public class ServiceProviderSecurityAspect {

    private final ServiceEmployeeRoleRepository serviceEmployeeRoleRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public ServiceProviderSecurityAspect(ServiceEmployeeRoleRepository serviceEmployeeRoleRepository, UserProfileRepository userProfileRepository) {
        this.serviceEmployeeRoleRepository = serviceEmployeeRoleRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Around("@annotation(serviceProviderSecurity)")
    public Object validateAccess(ProceedingJoinPoint joinPoint, ServiceProviderSecurity serviceProviderSecurity) throws Throwable {
        String paramName = serviceProviderSecurity.serviceProviderIdParam();
        List<String> roles = List.of(serviceProviderSecurity.requiredRoles());

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Long providerId = null;

        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(paramName) && args[i] instanceof Long) {
                providerId = (Long) args[i];
                break;
            }
        }

        if (providerId == null) {
            throw new IllegalArgumentException("Missing or invalid provider ID in method parameters.");
        }

        UUID userId = AuthUtils.getLoggedInUserId();

        UserProfile userProfile = userProfileRepository.findByKeycloakUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User profile not found."));
        boolean authorized = serviceEmployeeRoleRepository.existsByUserProfileIdAndServiceProviderIdAndRoleIn(userProfile.getId(), providerId, roles);

        if (!authorized) {
            throw new AccessDeniedException("Not authorized to access this service provider");
        }

        return joinPoint.proceed();
    }
}
