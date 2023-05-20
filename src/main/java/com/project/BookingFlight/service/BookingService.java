package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Booking;

import java.util.List;

public interface BookingService {
    void deleteBooking(Long id);
    BookingDTO findBookingById(Long id);
    BookingDTO saveNewBooking(Booking booking);
    List<BookingDTO> showAllBookings(Long id);
    List<BookingDTO> getAllBookingsByTraveller(Long travellerId);
    BookingDTO saveNewBookingForUser(Long userId, Booking booking);
}
