package com.alhas.airbnb.listing.mapper;


import com.alhas.airbnb.listing.application.dto.CreatedListingDTO;
import com.alhas.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.alhas.airbnb.listing.application.dto.SaveListingDTO;
import com.alhas.airbnb.listing.application.dto.sub.ListingInfoDTO;
import com.alhas.airbnb.listing.application.dto.vo.PriceVO;
import com.alhas.airbnb.listing.domain.Listing;
import com.nimbusds.openid.connect.sdk.Display;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ListingPictureMapper.class})
public interface ListingMapper {

    @Mapping(target = "landlordPublicId", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "pictures", ignore = true)
    @Mapping(target = "title", source = "description.title.value")
    @Mapping(target = "description", source = "description.description.value")
    @Mapping(target = "bedrooms", source = "infos.bedrooms.value")
    @Mapping(target = "guests", source = "infos.guests.value")
    @Mapping(target = "bookingCategory", source = "category")
    @Mapping(target = "beds", source = "infos.beds.value")
    @Mapping(target = "bathrooms", source = "infos.baths.value")
    @Mapping(target = "price", source = "price.value")
    //Listing savelistingDTOToListing(SaveListingDTO saveListinglistingDTO);
       Listing saveListingDTOToListing(SaveListingDTO saveListingDTO);

    CreatedListingDTO listingToCreatedLinstingDTO(Listing listing);

    @Mapping(target = "cover", source = "pictures")
    List<DisplayCardListingDTO> listingToDisplayCardListingDTO(List<Listing> listing);

@Mapping(target = "cover", source = "pictures", qualifiedByName = "extract-cover")
DisplayCardListingDTO listingToDisplayCardListingDTO(Listing listing);

default PriceVO  mapPriceToPriveVO(int price) {

    return new PriceVO(price);
}



}


