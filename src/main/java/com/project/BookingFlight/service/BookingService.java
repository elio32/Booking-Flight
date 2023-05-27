package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Pagination;

import java.util.List;

public interface BookingService {
    List<BookingDTO> getAllBookingsByTraveller(Long travellerId);
    BookingDTO requestBookingCancellation(Long bookingId);
    List<BookingDTO> getUserBookings(Long userId, Pagination pagination);
    BookingDTO confirmBookingCancellation(Long bookingId, BookingDTO bookingDTO);
    BookingDTO createBooking(Booking booking, String email);
}
