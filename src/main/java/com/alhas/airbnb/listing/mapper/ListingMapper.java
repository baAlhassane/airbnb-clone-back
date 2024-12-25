package com.alhas.airbnb.listing.mapper;


import com.alhas.airbnb.listing.application.dto.CreatedListingDTO;
import com.alhas.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.alhas.airbnb.listing.application.dto.DisplayListingDTO;
import com.alhas.airbnb.listing.application.dto.SaveListingDTO;
import com.alhas.airbnb.listing.application.dto.sub.ListingInfoDTO;
import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.listing.application.dto.vo.PriceVO;
import com.alhas.airbnb.listing.domain.Listing;
import com.alhas.airbnb.listing.domain.ListingPicture;
import com.nimbusds.openid.connect.sdk.Display;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

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
    @Mapping(target = "category", source = "category")
    @Mapping(target = "beds", source = "infos.beds.value")
    @Mapping(target = "bathrooms", source = "infos.baths.value")
    @Mapping(target = "price", source = "price.value")
    //Listing savelistingDTOToListing(SaveListingDTO saveListinglistingDTO);
       Listing saveListingDTOToListing(SaveListingDTO saveListingDTO);
  //  return listingMapper.listingToDisplayCardListingDTOs(properties);
    CreatedListingDTO listingToCreatedLinstingDTO(Listing listing);

    @Mapping(target = "cover", source = "pictures")
    List<DisplayCardListingDTO> listingToDisplayCardListingDTOs(List<Listing> listings);

    @Mapping(target = "cover", source = "pictures", qualifiedByName = "extract-cover")
    DisplayCardListingDTO listingToDisplayCardListingDTO(Listing listing);


default PriceVO  mapPriceToPriveVO(int price) {

    return new PriceVO(price);
}

@Mapping(target = "landlord", ignore = true)
@Mapping(target = "description.title.value", source = "title")
@Mapping(target = "description.description.value", source = "description")
@Mapping(target = "infos.bedrooms.value", source = "bedrooms")
@Mapping(target = "infos.beds.value", source = "beds")
@Mapping(target = "infos.baths.value", source = "bathrooms")
@Mapping(target = "infos.guests.value", source = "guests")
@Mapping(target = "category", source = "category")
@Mapping(target = "price.value", source = "price")
DisplayListingDTO listingToDisplayListingDTO(Listing listing);


//    @Mapping(target = "isCover", source = "cover")
//    PictureDTO convertToPictureDTO(ListingPicture picture);// Si on le decommente ca met en confusion du mappage de ListingPicture a PictureDTO



}


