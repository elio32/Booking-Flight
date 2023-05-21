package com.project.BookingFlight.service.impl;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.mapper.BookingMapper;
import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Pagination;
import com.project.BookingFlight.model.entity.UserApp;
import com.project.BookingFlight.model.enums.UserRoleEnum;
import com.project.BookingFlight.repository.BookingRepository;
import com.project.BookingFlight.repository.UserRepository;
import com.project.BookingFlight.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingDTO> getAllBookingsByTraveller(Long travellerId) {
        Optional<UserApp> traveller = userRepository.findById(travellerId);
        checkIfUserExist(traveller);
        List<Booking> bookings = bookingRepository.findBookingsByUser(traveller.get());
        return bookings.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    public BookingDTO requestBookingCancellation(Long bookingId, String declineReason, UserRoleEnum userRole) {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        checkIfBookingExist(existingBooking);
        Booking booking = existingBooking.get();

        // Check if booking is already cancelled
        if (booking.isCancelled()) {
            throw new GeneralException("Booking has already been cancelled.", Arrays.asList(existingBooking));
        }
        // Handle traveler's cancellation request
        if (userRole == UserRoleEnum.TRAVELLER) {
            // Update booking status to "Cancellation Requested"
            booking.setCancelled(true);
            booking.setCancellationReason(null);
        }
        // Handle admin decline cancellation request
        if (userRole == UserRoleEnum.ADMIN && declineReason != null && !declineReason.isEmpty()) {
            // Update booking status to "Declined" and set the decline reason
            booking.setCancelled(false);
            booking.setCancellationReason(declineReason);
        }
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDTO> getUserBookings(Long userId, Pagination pagination) {
        if(pagination == null) pagination = new Pagination();
        log.info("Fetching all Users with {}", pagination.toString());
        Pageable pageable = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(),
                pagination.getSortByAscendingOrder() ? Sort.by("flight.departureDate").ascending()
                        : Sort.by("flight.departureDate").descending());
        Page<Booking> bookingPage = bookingRepository.findByUserId(userId,pageable);// mbas pageable me duket duhet .getContent
        List<BookingDTO> bookingDTO = bookingPage.stream().map(bookingMapper::toDto).collect(Collectors.toList());
        return bookingDTO;
    }

    @Override
    public BookingDTO createBooking(Booking booking) {


        return null;

    }


    private void checkIfBookingExist(Optional<Booking> booking) {
        log.info("Checking if booking exists");
        if (booking.isEmpty()) {
            log.error("Booking not found");
            throw new GeneralException("Booking not found", null);
        }
    }
    // se di a duhet ta bej checkIfUserExist ktu
    private void checkIfUserExist(Optional<UserApp> user) {
        if (user.isEmpty() || !user.get().isEnabled()) {
            if (user.isEmpty())
                log.error("User not found");
            else
                log.error("User is not enabled");
            throw new GeneralException("User not found", null);
        }
    }
}
