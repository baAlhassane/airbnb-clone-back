package com.alhas.airbnb.listing;

import com.alhas.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.alhas.airbnb.listing.application.dto.DisplayListingDTO;
import com.alhas.airbnb.listing.application.dto.LandlordService;
import com.alhas.airbnb.listing.application.dto.sub.LandlordListingDTO;
import com.alhas.airbnb.listing.domain.BookingCategory;
import com.alhas.airbnb.listing.domain.Listing;
import com.alhas.airbnb.listing.mapper.ListingMapper;
import com.alhas.airbnb.listing.repository.ListingRepository;
import com.alhas.airbnb.sharedkernel.service.State;
import com.alhas.airbnb.user.application.dto.ReadUserDTO;
import com.alhas.airbnb.user.application.dto.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TenantService {
    private final ListingMapper listingMapper;
    private final ListingRepository listingRepository;
    private final UserService userService;


    public TenantService(ListingMapper listingMapper, ListingRepository listingRepository, UserService userService) {
        this.listingMapper = listingMapper;
        this.listingRepository = listingRepository;
        this.userService = userService;

    }

    public Page<DisplayCardListingDTO> getAllByCategory(Pageable pageable, BookingCategory category) {
        Page<Listing> allBookingCategory;
        if(category == BookingCategory.ALL){
            allBookingCategory = listingRepository.findAllWithCoverOnly(pageable);
        }

        else {
            allBookingCategory= listingRepository.findAllByBookingCategoryWithCoverOnly(pageable,category);
        }

        return allBookingCategory.map(listingMapper::listingToDisplayCardListingDTO);

    }

    @Transactional(readOnly = true)
    public State<DisplayListingDTO, String> getOne(UUID publicId) {
       Optional<Listing> listingByPublicIdOp = listingRepository.findByPublicId(publicId);
       if(listingByPublicIdOp.isEmpty()){
           return State.<DisplayListingDTO,String>builder().
                   forError(String.format("Listing does not exist for publicId:  %s", publicId));
       }

       DisplayListingDTO displayListingDT= listingMapper.listingToDisplayListingDTO(listingByPublicIdOp.get());

        ReadUserDTO readUserDTO = userService.getByPublicId(listingByPublicIdOp.get().getLandlordPublicId()).orElseThrow();

        LandlordListingDTO landlordListingDTO=new LandlordListingDTO(readUserDTO.getFirstName(), readUserDTO.getImageUrl());

        displayListingDT.setLandlord(landlordListingDTO);

        return  State.<DisplayListingDTO, String> builder().forSuccess(displayListingDT);

    }



}
