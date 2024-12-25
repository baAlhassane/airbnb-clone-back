package com.alhas.airbnb.listing.application.dto;

import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.listing.application.dto.vo.PriceVO;
import com.alhas.airbnb.listing.domain.BookingCategory;

import java.util.UUID;

public record DisplayCardListingDTO(
        PriceVO price,
        String location,
        PictureDTO cover,
        BookingCategory category,
        UUID publicId


) {
}
