package com.muyategna.backend.service_provider.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
@Documented
public @interface ServiceProviderSecurity {
    String serviceProviderIdParam() default "serviceProviderId";

    String[] requiredRoles() default {"MANAGER"};
}
