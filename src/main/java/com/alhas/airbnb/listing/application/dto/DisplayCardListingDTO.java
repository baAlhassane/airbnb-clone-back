package com.alhas.airbnb.listing.application.dto;

import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.listing.application.dto.vo.PriceVO;

public record DisplayCardListingDTO(
        PriceVO proce,
        String location,
        PictureDTO cover
) {
}
