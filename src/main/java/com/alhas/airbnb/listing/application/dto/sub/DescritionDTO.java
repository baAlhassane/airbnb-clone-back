package com.alhas.airbnb.listing.application.dto.sub;

import com.alhas.airbnb.listing.application.dto.vo.DescriptionVO;
import com.alhas.airbnb.listing.application.dto.vo.TitleVO;
import jakarta.validation.constraints.NotNull;

public record DescritionDTO(
        @NotNull TitleVO titleVO,
        @NotNull DescriptionVO descriptionVO

        
) {
}
