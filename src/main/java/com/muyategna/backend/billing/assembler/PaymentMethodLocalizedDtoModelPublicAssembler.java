package com.muyategna.backend.billing.assembler;

import com.muyategna.backend.billing.dto.payment_method.PaymentMethodLocalizedDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class PaymentMethodLocalizedDtoModelPublicAssembler {
    public EntityModel<PaymentMethodLocalizedDto> toModel(PaymentMethodLocalizedDto paymentMethodLocalizedDto, HttpServletRequest request) {
        return EntityModel.of(paymentMethodLocalizedDto);
    }


    public CollectionModel<EntityModel<PaymentMethodLocalizedDto>> toCollectionModel(Iterable<? extends PaymentMethodLocalizedDto> paymentMethodLocalizedDtoList, HttpServletRequest request) {
        List<EntityModel<PaymentMethodLocalizedDto>> paymentMethodLocalizedDtosModel = StreamSupport.stream(paymentMethodLocalizedDtoList.spliterator(), false)
                .map(paymentMethodLocalizedDto -> toModel(paymentMethodLocalizedDto, request))
                .toList();
        return CollectionModel.of(paymentMethodLocalizedDtosModel);
    }
}
