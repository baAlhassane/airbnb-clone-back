package com.alhas.airbnb.listing.mapper;

import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.listing.domain.ListingPicture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ListingPictureMapper {
//    @Mapping(target = "listingId",ignore = true)
//    @Mapping(target = "listing",ignore = true)
    Set<ListingPicture> pictureDTOsToListingPicture(List<PictureDTO> pictureDTOList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "listing", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "cover", source = "isCover")
    @Mapping(target = "picture", source = "file")
    @Mapping(target = "fileContType", source = "fileContentType") // Mappage pour le content type
    ListingPicture pictureDTOToListingPicture(PictureDTO pictureDTO);

    List<PictureDTO> listingPictureToPictureDTO(List<ListingPicture> listingPictureList);
  @Mapping(target = "isCover",source = "cover")
    PictureDTO convertToPictureDTO(ListingPicture listingPicture);

    @Named("extract-cover")
    default PictureDTO extractCover(Set<ListingPicture> pictureSet) {
        return pictureSet.stream()
                .findFirst()
                .map(this::convertToPictureDTO)
                .orElseThrow();
    }

}






