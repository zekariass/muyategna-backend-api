package com.muyategna.backend.billing.assembler;

import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class SubscriptionPlanLocalizedDtoModelPublicAssembler {

    public EntityModel<SubscriptionPlanLocalizedDto> toModel(SubscriptionPlanLocalizedDto subscriptionPlanLocalizedDto,
                                                             HttpServletRequest request) {
        return EntityModel.of(subscriptionPlanLocalizedDto);
    }


    public CollectionModel<EntityModel<SubscriptionPlanLocalizedDto>> toCollectionModel(Iterable<? extends SubscriptionPlanLocalizedDto> subscriptionPlanLocalizedDtos,
                                                                                        HttpServletRequest request) {
        List<EntityModel<SubscriptionPlanLocalizedDto>> subscriptionPlanLocalizedDtoList = StreamSupport.stream(subscriptionPlanLocalizedDtos.spliterator(), false)
                .map(subscriptionPlanLocalizedDto -> this.toModel(subscriptionPlanLocalizedDto, request))
                .toList();
        return CollectionModel.of(subscriptionPlanLocalizedDtoList);
    }
}
