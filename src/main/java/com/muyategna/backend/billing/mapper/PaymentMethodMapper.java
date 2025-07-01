package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.payment_method.PaymentMethodCreateDto;
import com.muyategna.backend.billing.dto.payment_method.PaymentMethodDto;
import com.muyategna.backend.billing.dto.payment_method.PaymentMethodLocalizedDto;
import com.muyategna.backend.billing.dto.payment_method.PaymentMethodUpdateDto;
import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.PaymentMethodTranslation;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.system.exception.ResourceNotFoundException;

import java.util.List;

public final class PaymentMethodMapper {

    public static PaymentMethodDto toDto(com.muyategna.backend.billing.entity.PaymentMethod paymentMethod) {
        return PaymentMethodDto.builder()
                .id(paymentMethod.getId())
                .countryId(paymentMethod.getCountry().getId())
                .name(paymentMethod.getName())
                .createdAt(paymentMethod.getCreatedAt())
                .updatedAt(paymentMethod.getUpdatedAt())
                .build();
    }

    public static PaymentMethodLocalizedDto toLocalizedDto(PaymentMethod paymentMethod,
                                                           PaymentMethodTranslation translation) {
        return PaymentMethodLocalizedDto.builder()
                .id(paymentMethod.getId())
                .countryId(paymentMethod.getCountry().getId())
                .displayName(translation.getDisplayName())
                .description(translation.getDescription())
                .build();
    }


    public static List<PaymentMethodLocalizedDto> toLocalizedDtoList(
            List<PaymentMethod> paymentMethods,
            List<PaymentMethodTranslation> translations) {

        return paymentMethods.stream()
                .map(paymentMethod -> {
                    PaymentMethodTranslation translation = translations.stream()
                            .filter(t -> t.getPaymentMethod().getId().equals(paymentMethod.getId()))
                            .findFirst()
                            .orElse(null);

                    if (translation == null) {
                        throw new ResourceNotFoundException("Payment method not found for one of the payment method translation");
                    }
                    return toLocalizedDto(paymentMethod, translation);
                })
                .toList();
    }


    public static PaymentMethod toEntity(PaymentMethodDto paymentMethodDto,
                                         Country country) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodDto.getId());
        paymentMethod.setName(paymentMethodDto.getName());
        paymentMethod.setCountry(country);
        return paymentMethod;
    }


    public static PaymentMethod toEntity(PaymentMethodCreateDto paymentMethodDto,
                                         Country country) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(paymentMethodDto.getName());
        paymentMethod.setCountry(country);
        return paymentMethod;
    }


    public static PaymentMethod toEntity(PaymentMethodUpdateDto paymentMethodDto,
                                         Country country) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(paymentMethodDto.getName());
        paymentMethod.setCountry(country);
        return paymentMethod;
    }
}
