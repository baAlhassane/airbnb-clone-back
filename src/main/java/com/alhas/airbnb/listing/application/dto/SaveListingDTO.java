package com.alhas.airbnb.listing.application.dto;

import com.alhas.airbnb.listing.application.dto.sub.DescritionDTO;
import com.alhas.airbnb.listing.application.dto.sub.ListingInfoDTO;
import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.listing.application.dto.vo.PriceVO;

import com.alhas.airbnb.listing.domain.BookingCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SaveListingDTO {
    @NotNull
    BookingCategory bookingCategory;
    @NotNull
    String location;
    @NotNull
    @Valid
    ListingInfoDTO listingInfoDTO;
    @NotNull
    @Valid
    DescritionDTO descriptionDTO;
    @NotNull
    @Valid
    PriceVO priceVO;
    @NotNull
    List<PictureDTO> picturesDTO;

    public @NotNull BookingCategory getBookingCategory() {
        return bookingCategory;
    }

    public void setBookingCategory(@NotNull BookingCategory bookingCategory) {
        this.bookingCategory = bookingCategory;
    }

    public @NotNull String getLocation() {
        return location;
    }

    public void setLocation(@NotNull String location) {
        this.location = location;
    }

    public @NotNull @Valid ListingInfoDTO getListingInfoDTO() {
        return listingInfoDTO;
    }

    public void setListingInfoDTO(@NotNull @Valid ListingInfoDTO listingInfoDTO) {
        this.listingInfoDTO = listingInfoDTO;
    }

    public @NotNull @Valid DescritionDTO getDescriptionDTO() {
        return descriptionDTO;
    }

    public void setDescriptionDTO(@NotNull @Valid DescritionDTO descriptionDTO) {
        this.descriptionDTO = descriptionDTO;
    }

    public @NotNull @Valid PriceVO getPriceVO() {
        return priceVO;
    }

    public void setPriceVO(@NotNull @Valid PriceVO priceVO) {
        this.priceVO = priceVO;
    }

    public @NotNull List<PictureDTO> getPicturesDTO() {
        return picturesDTO;
    }

    public void setPicturesDTO(@NotNull List<PictureDTO> picturesDTO) {
        this.picturesDTO = picturesDTO;
    }
}