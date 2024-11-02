package com.alhas.airbnb.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;

public record DescriptionVO(
        @NotNull(message ="Desceiption value must be present") int value
) {
}
