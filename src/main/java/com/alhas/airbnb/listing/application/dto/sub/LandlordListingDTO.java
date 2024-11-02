package com.alhas.airbnb.listing.application.dto.sub;

import jakarta.validation.constraints.NotNull;

public record LandlordListingDTO(
        @NotNull
        String firstname,
        @NotNull
        String imageUrl
) {
}
