package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Pagination;
import com.project.BookingFlight.model.enums.UserRoleEnum;

import java.util.List;

public interface BookingService {
    List<BookingDTO> getAllBookingsByTraveller(Long travellerId);
    BookingDTO requestBookingCancellation(Long bookingId, String declineReason, UserRoleEnum userRole);
    List<BookingDTO> getUserBookings(Long userId, Pagination pagination);
    BookingDTO createBooking(Booking booking);
}
