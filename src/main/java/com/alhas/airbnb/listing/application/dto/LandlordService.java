package com.alhas.airbnb.listing.application.dto;

import com.alhas.airbnb.listing.domain.Listing;
import com.alhas.airbnb.listing.mapper.ListingMapper;
import com.alhas.airbnb.listing.repository.ListingRepository;
import com.alhas.airbnb.sharedkernel.service.State;
import com.alhas.airbnb.user.application.dto.Auth0Service;
import com.alhas.airbnb.user.application.dto.ReadUserDTO;
import com.alhas.airbnb.user.application.dto.UserService;
import com.alhas.airbnb.user.domain.User;
import com.alhas.airbnb.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class LandlordService {

    private final ListingRepository listingRepository;

    private final ListingMapper  listingMapper;

    private final Auth0Service auth0Service;
    private final  UserService userService;
    private final PictureService pictureService;

    private final UserRepository userRepository;


    public LandlordService(ListingRepository listingRepository, ListingMapper listingMapper, Auth0Service auth0Service, UserService userService, PictureService pictureService, UserRepository userRepository) {
        this.listingRepository = listingRepository;
        this.listingMapper = listingMapper;
        this.auth0Service = auth0Service;
        this.userService = userService;
        this.pictureService = pictureService;
        this.userRepository = userRepository;
    }

    public CreatedListingDTO createdListingDTO(SaveListingDTO saveListingDTO) {

        Listing newListing =listingMapper.saveListingDTOToListing(saveListingDTO);
        ReadUserDTO userConnected = userService.getAuthenticatedUserFromSecurityContext();
        System.out.println(" ----------------  ");
        System.out.println(" userService.getAuthenticatedUserFromSecurityContext()  : "+ userService.getAuthenticatedUserFromSecurityContext());
        System.out.println(" new listing : "+newListing.getPublicId()+" "+newListing.getTitle());
        System.out.println(" newlinting.getlandlord : "+newListing.getLandlordPublicId()+" "+newListing.getTitle());
        System.out.println(" new userconnect public Id : "+userConnected);
        System.out.println(" ----------------  ");

        Optional<User> user= userRepository.findOneByEmail(userConnected.getEmail());
        System.out.println("   ");
        System.out.println(" ----------------  ");
        System.out.println(" user in  createdListingDTO(SaveListingDTO saveListingDTO) : "+user);
        System.out.println(" ----------------  ");

        userConnected.setPublicI(user.get().getPublicId());

        newListing.setLandlordPublicId(user.get().getPublicId());
       // newListing.setPublicId(user.get().getPublicId());


        Listing savaListing = listingRepository.saveAndFlush(newListing);

        pictureService.SaveAll(saveListingDTO.getPictures(),savaListing);

        auth0Service.addLandlordRoleToUser(userConnected);

        return listingMapper.listingToCreatedLinstingDTO(savaListing);
    }


    @Transactional(readOnly = true)
    public List<DisplayCardListingDTO> getAllProperties(ReadUserDTO landlord) {
        List<Listing> properties = listingRepository.findAllByLandlordPublicId(landlord.getPublicI());
        return listingMapper.listingToDisplayCardListingDTO(properties);
    }


    @Transactional
    public State<UUID, String> delete(UUID publicId, ReadUserDTO landlord) {
        long deletedSuccessfully = listingRepository.deleteByPublicIdAndLandlordPublicId(publicId, landlord.getPublicI());
        if(deletedSuccessfully >0){
            return State.<UUID, String>builder().forSuccess(publicId);
        }
        else{
            return State.<UUID, String>builder().forUnauthorized("user not authorized to delete this listing ");
        }

    }
}
