package com.muyategna.backend.system.config;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.system.converter.CustomRoleConverter;
import com.muyategna.backend.system.exception.CustomAccessDeniedHandler;
import com.muyategna.backend.system.exception.CustomAuthenticationEntryPoint;
import com.muyategna.backend.system.filter.CountryFilter;
import com.muyategna.backend.system.filter.LanguageFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Security configuration for the system module.
 * <p>
 * This configuration is only active when the 'prod' profile is not active.
 * It sets up CORS, CSRF, and JWT authentication for the application.
 */
@Profile("!prod")
@Configuration
@EnableMethodSecurity(proxyTargetClass = true)
public class SystemSecurityConfig {

    private final LanguageFilter languageFilter;
    private final CountryFilter countryFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomRoleConverter customRoleConverter;

    public SystemSecurityConfig(LanguageFilter languageFilter, CountryFilter countryFilter, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomRoleConverter customRoleConverter) {
        this.languageFilter = languageFilter;
        this.countryFilter = countryFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customRoleConverter = customRoleConverter;
    }

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(customRoleConverter);
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
//                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080"));
                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(List.of("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                })).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/", "/api/v1/users", "/api/v1/users/**", "/api/v1/location/**", "/api/v1/system/languages/**").permitAll()
//                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resource/**",
//                                "/webjars/**", "/docs").permitAll()
                                .requestMatchers(Constants.PUBLIC_API_ENDPOINTS_FOR_ACCESS.toArray(new String[0])).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/public/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/public/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Pre-flight request
                                .anyRequest().authenticated()
                );
        http.exceptionHandling(exc -> exc
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(authenticationEntryPoint)
                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)
                ));
        http.addFilterAfter(countryFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(languageFilter, CountryFilter.class);

        return http.build();

    }


//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:8080"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setExposedHeaders(List.of("Authorization"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//        source.registerCorsConfiguration("/**", config);
//
//        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Run before Spring Security
//        return bean;
//    }
}
