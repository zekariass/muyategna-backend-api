package com.muyategna.backend.system.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Keycloak.
 * <p>
 * This class is used to bind the properties defined in the application.properties file under the "keycloak" prefix.
 * It contains properties related to Keycloak configuration, such as realm, endpoint, client ID, and client secret.
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "keycloak", ignoreUnknownFields = false)
public class KeycloakPropertiesConfig {
    private String realm;

    private String endpoint;

    private final Application application = new Application();

    /**
     * Inner class representing the application properties for Keycloak.
     * <p>
     * This class contains properties related to the Keycloak application, such as client ID and client secret.
     * </p>
     */
    @Getter
    @Setter
    public static class Application {
        private String clientId;
        private String clientSecret;
    }
}
