package com.alhas.airbnb.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;

public record TitleVO(
        @NotNull(message ="Titlevalue must be present") int value
) {
}