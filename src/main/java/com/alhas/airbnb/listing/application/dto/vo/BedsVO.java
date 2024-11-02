package com.alhas.airbnb.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;

public record BedsVO(
        @NotNull(message ="Bed value must be present") int value
) {
}
