package com.alhas.airbnb.listing.repository;

import com.alhas.airbnb.listing.domain.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {

}
