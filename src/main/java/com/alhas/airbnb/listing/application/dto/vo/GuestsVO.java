package com.alhas.airbnb.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;

public record GuestsVO(@NotNull(message ="Guess value must be present") int value) {
}
