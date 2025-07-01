package com.muyategna.backend.billing.assembler;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class DiscountPlanDtoModelPublicAssembler {
    public EntityModel<DiscountPlanLocalizedDto> toModel(DiscountPlanLocalizedDto discountPlanDto,
                                                         HttpServletRequest request) {

        return EntityModel.of(discountPlanDto);
    }


    public CollectionModel<EntityModel<DiscountPlanLocalizedDto>> toCollectionModel(Iterable<? extends DiscountPlanLocalizedDto> discountPlanDtoList, HttpServletRequest request) {
        List<EntityModel<DiscountPlanLocalizedDto>> discountPlanDtoListModel = StreamSupport.stream(discountPlanDtoList.spliterator(), false)
                .map(discountPlanDto -> this.toModel(discountPlanDto, request))
                .toList();
        return CollectionModel.of(discountPlanDtoListModel);
    }
}
