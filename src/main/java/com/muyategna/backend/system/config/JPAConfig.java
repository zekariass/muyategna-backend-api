package com.muyategna.backend.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Configuration class for JPA settings.
 * <p>
 * This class enables JPA Auditing and Web Security configuration for the application.
 * It is annotated with @Configuration to indicate that it is a source of bean definitions.
 * </p>
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableWebSecurity
@Configuration
public class JPAConfig {

}
