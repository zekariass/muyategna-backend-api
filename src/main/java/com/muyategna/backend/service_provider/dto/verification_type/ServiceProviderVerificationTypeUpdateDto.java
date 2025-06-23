package com.muyategna.backend.service_provider.dto.verification_type;

import com.muyategna.backend.service_provider.enums.ServiceProviderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderVerificationTypeUpdateDto {
    @NotNull(message = "Name is required")
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Provider type is required")
    private ServiceProviderType providerType;
    private Boolean isMandatory;
    private Boolean documentRequired;
}