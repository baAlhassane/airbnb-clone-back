package com.alhas.airbnb.listing.application.dto.sub;

import com.alhas.airbnb.listing.application.dto.vo.BathsVO;
import com.alhas.airbnb.listing.application.dto.vo.BedroomsVO;
import com.alhas.airbnb.listing.application.dto.vo.GuestsVO;
import com.alhas.airbnb.listing.application.dto.vo.BedsVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ListingInfoDTO(
        @NotNull @Valid GuestsVO guestsVO,
        @NotNull @Valid BedroomsVO bedroomsVO,
        @NotNull @Valid BedsVO bedsVO,
        @NotNull @Valid BathsVO bathsVO) {
}
