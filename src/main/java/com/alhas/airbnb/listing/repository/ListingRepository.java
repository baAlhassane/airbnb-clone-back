package com.alhas.airbnb.listing.repository;

import com.alhas.airbnb.listing.domain.Listing;
import com.alhas.airbnb.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, Long> {

}
