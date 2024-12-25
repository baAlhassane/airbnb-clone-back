package com.alhas.airbnb.listing.repository;

import com.alhas.airbnb.listing.domain.BookingCategory;
import com.alhas.airbnb.listing.domain.Listing;
import com.alhas.airbnb.user.application.dto.ReadUserDTO;
import com.alhas.airbnb.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture"+
            " WHERE listing.landlordPublicId = :landlordPublicId AND picture.isCover = true")
    List<Listing> findAllByLandlordPublicIdFetchCoverPicture(UUID landlordPublicId);



    long deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlord);


    @Query("SELECT listing from Listing listing LEFT JOIN FETCH listing.pictures picture"+
            " WHERE  picture.isCover=true AND listing.category = :bookingCategory ")
    Page<Listing> findAllByBookingCategoryWithCoverOnly(Pageable pageable, BookingCategory bookingCategory);


    @Query("SELECT listing from Listing listing LEFT JOIN FETCH listing.pictures picture"+
            " WHERE  picture.isCover=true")
    Page<Listing> findAllWithCoverOnly(Pageable pageable);

    Optional<Listing> findByPublicId(UUID publicId);

}
