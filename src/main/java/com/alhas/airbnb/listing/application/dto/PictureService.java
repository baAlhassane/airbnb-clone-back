package com.alhas.airbnb.listing.application.dto;

import com.alhas.airbnb.listing.application.dto.sub.PictureDTO;
import com.alhas.airbnb.listing.domain.Listing;
import com.alhas.airbnb.listing.domain.ListingPicture;
import com.alhas.airbnb.listing.mapper.ListingPictureMapper;
import com.alhas.airbnb.listing.repository.ListingPictureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class PictureService {
    private final ListingPictureRepository listingPictureRepository;

    private final ListingPictureMapper listingPictureMapper;

    public PictureService(ListingPictureRepository listingPictureRepository, ListingPictureMapper listingPictureMapper) {
        this.listingPictureRepository = listingPictureRepository;
        this.listingPictureMapper = listingPictureMapper;
    }

    public List<PictureDTO> SaveAll(List<PictureDTO> pictures, Listing listing) {
       Set<ListingPicture> listingPictures= listingPictureMapper.pictureDTOsToListingPicture(pictures);

        boolean isFirst = true;
        for(ListingPicture listingPicture: listingPictures){
            listingPicture.setListing(listing);
            listingPicture.setCover(isFirst);
            isFirst = false;
        }
          listingPictureRepository.saveAll(listingPictures);
        return listingPictureMapper.listingPictureToPictureDTO(listingPictures.stream().toList());
    }
}
