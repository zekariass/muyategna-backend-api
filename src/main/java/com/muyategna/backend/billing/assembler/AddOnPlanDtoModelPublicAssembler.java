package com.muyategna.backend.billing.assembler;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class AddOnPlanDtoModelPublicAssembler {

    public EntityModel<AddOnPlanLocalizedDto> toModel(AddOnPlanLocalizedDto addOnPlanLocalizedDto,
                                                      HttpServletRequest request) {
        return EntityModel.of(addOnPlanLocalizedDto);
    }

    public CollectionModel<EntityModel<AddOnPlanLocalizedDto>> toCollectionModel(Iterable<? extends AddOnPlanLocalizedDto> addOnPlanDtoList,
                                                                                 HttpServletRequest request) {
        List<EntityModel<AddOnPlanLocalizedDto>> addOnPlanDtoListModel = StreamSupport.stream(addOnPlanDtoList.spliterator(), false)
                .map(addOnPlanDto -> this.toModel(addOnPlanDto, request))
                .toList();
        return CollectionModel.of(addOnPlanDtoListModel);
    }
}
