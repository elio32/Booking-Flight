package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Pagination;
import com.project.BookingFlight.model.enums.BookingClassesEnum;

import java.util.List;

public interface BookingService {
    List<BookingDTO> getAllBookingsByTraveller(Long travellerId);
    BookingDTO requestBookingCancellation(Long bookingId);
    List<BookingDTO> getUserBookings(Long userId, Pagination pagination);
    BookingDTO confirmBookingCancellation(Long bookingId, String declineReason);
    BookingDTO createBooking(Long flightIds, BookingClassesEnum bookingClass);
}
