package com.muyategna.backend.system.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Keycloak admin client.
 *
 * <p>This class provides a Keycloak instance which can be used to interact with Keycloak REST API.
 *
 * @author Zekarias Negese
 */
@RequiredArgsConstructor
@Configuration
public class KeycloakAdminConfig {

    /**
     * Configuration properties for Keycloak.
     */
    private final KeycloakPropertiesConfig propertiesConfig;

    /**
     * Creates a Keycloak instance configured with the properties from KeycloakPropertiesConfig.
     *
     * @return a Keycloak instance
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(propertiesConfig.getEndpoint())
                .realm(propertiesConfig.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(propertiesConfig.getApplication().getClientId())
                .clientSecret(propertiesConfig.getApplication().getClientSecret())
                .build();
    }

}
