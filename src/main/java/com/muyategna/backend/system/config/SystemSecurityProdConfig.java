package com.muyategna.backend.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration class for production environment.
 *
 * <p>This class is used to define beans and configurations specific to the production environment.
 *
 * @author Zekarias
 */
@Profile("prod")
@Configuration
public class SystemSecurityProdConfig {
}
