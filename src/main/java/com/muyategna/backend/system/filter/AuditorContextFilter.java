package com.muyategna.backend.system.filter;

import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.user.dto.UserProfileDto;
import com.muyategna.backend.user.mapper.UserProfileMapper;
import com.muyategna.backend.user.service.UserProfileService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class AuditorContextFilter extends OncePerRequestFilter {

    @Autowired
    private UserProfileService userProfileService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                try {
                    UUID keycloakUserId = UUID.fromString(jwt.getSubject());
                    UserProfileDto userProfileDto =userProfileService.findByKeycloakUserId(keycloakUserId);

                    if (userProfileDto != null) {
                        AuditorContextHolder.setUserProfile(UserProfileMapper.toEntity(userProfileDto, null));
                    }
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid UUID format for JWT subject: {}", jwt.getSubject(), e);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            AuditorContextHolder.clear(); // Clean up after request
        }
    }
}
