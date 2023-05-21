package com.project.BookingFlight.repository;

import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.model.entity.UserApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findBookingsByUser(UserApp user);
    List<Booking> findBookingByFlights(Optional<Flight> flight); //????

    List<Booking> findByFlights(Flight flight);
    Page<Booking> findByUserId(Long userId, Pageable pageable);//????

}
