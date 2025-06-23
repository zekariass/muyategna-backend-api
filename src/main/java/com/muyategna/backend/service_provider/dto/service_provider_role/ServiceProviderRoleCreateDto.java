package com.muyategna.backend.service_provider.dto.service_provider_role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderRoleCreateDto {

    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}