package com.alhas.airbnb.listing.application.dto.sub;

import com.alhas.airbnb.listing.application.dto.vo.DescriptionVO;
import com.alhas.airbnb.listing.application.dto.vo.TitleVO;
import jakarta.validation.constraints.NotNull;

public record DescriptionDTO(
        @NotNull TitleVO title,
        @NotNull DescriptionVO description
) {
}
