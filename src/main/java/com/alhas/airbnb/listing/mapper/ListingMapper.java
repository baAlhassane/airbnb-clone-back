package com.alhas.airbnb.listing.mapper;


import com.alhas.airbnb.listing.application.dto.CreatedListingDTO;
import com.alhas.airbnb.listing.application.dto.SaveListingDTO;
import com.alhas.airbnb.listing.application.dto.sub.ListingInfoDTO;
import com.alhas.airbnb.listing.domain.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {ListingPictureMapper.class})
public interface ListingMapper {

    @Mapping(target = "landlordPublicId",ignore = true)
    @Mapping(target = "publicId",ignore = true)
    @Mapping(target = "lastModifiedDate",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createdDate",ignore = true)
    @Mapping(target = "pictures",ignore = true)
    @Mapping(target = "title",source = "descriptionDTO.titleVO.value")
    @Mapping(target = "description",source = "descriptionDTO.descriptionVO.value")
    @Mapping(target = "bedrooms",source = "listingInfoDTO.bedroomsVO.value")
    @Mapping(target = "guests",source = "listingInfoDTO.guestsVO.value")
    @Mapping(target = "bookingCategory",source = "bookingCategory")
    @Mapping(target = "beds",source = "listingInfoDTO.bedsVO.value")
    @Mapping(target = "bathrooms",source = "listingInfoDTO.bathsVO.value")
    @Mapping(target = "price",source = "priceVO.value")
    //bedroomsVO
    //Listing savelistingDTOToListing(SaveListingDTO saveListinglistingDTO);
       Listing saveListingDTOToListing(SaveListingDTO saveListingDTO);

    CreatedListingDTO listingToCreatedLinstingDTO(Listing listing);



}


