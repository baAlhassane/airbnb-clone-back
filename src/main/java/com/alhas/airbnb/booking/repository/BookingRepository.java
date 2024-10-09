package com.alhas.airbnb.booking.repository;

import com.alhas.airbnb.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
