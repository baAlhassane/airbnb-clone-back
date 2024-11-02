package com.alhas.airbnb.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;

public record BathsVO(
        @NotNull(message ="Baths value must be present")
        int value
) {

}
