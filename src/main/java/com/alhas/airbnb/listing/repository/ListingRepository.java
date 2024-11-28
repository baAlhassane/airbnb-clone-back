package com.alhas.airbnb.listing.repository;

import com.alhas.airbnb.listing.domain.Listing;
import com.alhas.airbnb.user.application.dto.ReadUserDTO;
import com.alhas.airbnb.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture"+
            " WHERE listing.landlordPublicId = :landlordPublicId AND picture.isCover= true")
    List<Listing> findAllByLandlordPublicId(UUID landlordPublicId);


    long deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlord);
}
