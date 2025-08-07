package com.muyategna.backend.professional_service.dto.service;

import com.muyategna.backend.common.enums.ServiceDeliveryMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceMinimalLocalizedDto {
    private Long id;
    private String name;
    private ServiceDeliveryMode deliveryMode;
}
