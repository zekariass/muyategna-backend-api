package com.muyategna.backend.location.config;

import com.uber.h3core.H3Core;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Spring configuration class to provide an H3Core instance as a Spring Bean.
 * This allows H3Core to be dependency-injected into other components.
 */
@Configuration
public class H3Config {

    /**
     * Creates and provides a singleton instance of H3Core.
     * This bean will be managed by Spring's IoC container.
     *
     * @return A new instance of H3Core.
     * @throws IOException If there is an error initializing H3Core.
     */
    @Bean
    public H3Core h3Core() throws IOException {
        // H3Core.newInstance() is the recommended way to get an instance.
        // Spring will manage this singleton bean.
        return H3Core.newInstance();
    }
}
