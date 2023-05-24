package com.project.BookingFlight.service.impl;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.mapper.BookingMapper;
import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.*;
import com.project.BookingFlight.model.enums.BookingClassesEnum;
import com.project.BookingFlight.repository.BookingRepository;
import com.project.BookingFlight.repository.FlightRepository;
import com.project.BookingFlight.repository.UserRepository;
import com.project.BookingFlight.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final FlightRepository flightRepository;

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

    @Override
    public BookingDTO requestBookingCancellation(Long bookingId) {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        checkIfBookingExist(existingBooking);
        Booking booking = existingBooking.get();

        if (booking.getIsCancelled()) {
            throw new GeneralException("Booking has already been cancelled.");
        }
        // Handle traveler's cancellation request
        booking.setAwaitingCancellation(true);

        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDTO confirmBookingCancellation(Long bookingId, String declineReason){
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        checkIfBookingExist(existingBooking);
        Booking booking = existingBooking.get();

        if (declineReason != null) {
            // Update booking status to "Declined" and set the decline reason
            booking.setIsCancelled(true);
            booking.setCancellationReason(declineReason);
        } else {
            booking.setIsCancelled(false);
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDTO> getUserBookings(Long userId, Pagination pagination) {
        if(pagination == null) pagination = new Pagination();
        log.info("Fetching all Users with {}", pagination);
        Pageable pageable = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(),
                pagination.getSortByAscendingOrder() ? Sort.by("flight.departureDate").ascending()
                        : Sort.by("flight.departureDate").descending());
        Page<Booking> bookingPage = bookingRepository.findByUserId(userId,pageable);
        return bookingPage.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override // shife se se di a eshte e sakte apo jo
    public BookingDTO createBooking(Long flightId, BookingClassesEnum bookingClass) {
        // Check if at least one flight is included in the booking
        if (flightId == null) {
            throw new GeneralException("At least one flight should be included in the booking.");
        }

        // Retrieve the Flight object from the database
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new GeneralException("Flight with ID " + flightId + " does not exist."));

        // Check if the flight has already departed
        Date currentDate = new Date();
        if (flight.getDepartureDate().before(currentDate)) {
            throw new GeneralException("Flight " + flight.getFlightNumber() + " has already departed.");
        }

        // Check if there are enough seats available in the requested booking class
        int availableSeats = getAvailableSeats(flight, bookingClass);
        if (availableSeats <= 0) {
            throw new GeneralException("Not enough seats available in the requested booking class for flight " + flight.getFlightNumber());
        }

        // Create the booking
        Booking booking = new Booking();
        booking.setIsCancelled(false);
        booking.setAwaitingCancellation(false);
        booking.setCancellationReason(null);
        booking.setBookingFlights(new ArrayList<>());

        // Create booking flight
        BookingFlight bookingFlight = new BookingFlight();
        bookingFlight.setBooking(booking);
        bookingFlight.setFlight(flight);
        bookingFlight.setBookingClass(bookingClass);

        booking.getBookingFlights().add(bookingFlight);

        Booking createdBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(createdBooking);
    }

    private int getAvailableSeats(Flight flight, BookingClassesEnum bookingClass) {
        switch (bookingClass) {
            case ECONOMY:
                return flight.getAvailableEconomySeats();
            case PREMIUM_ECONOMY:
                return flight.getAvailablePremiumEconomySeats();
            case BUSINESS:
                return flight.getAvailableBusinessSeats();
            case FIRST:
                return flight.getAvailableFirstClassSeats();
            default:
                return 0;
        }
    }


    private void checkIfBookingExist(Optional<Booking> booking) {
        log.info("Checking if booking exists");
        if (booking.isEmpty()) {
            log.error("Booking not found");
            throw new GeneralException("Booking not found");
        }
    }
    // se di a duhet ta bej checkIfUserExist ktu
    private void checkIfUserExist(Optional<UserApp> user) {
        if (user.isEmpty() || !user.get().isEnabled()) {
            if (user.isEmpty())
                log.error("User not found");
            else
                log.error("User is not enabled");
            throw new GeneralException("User not found");
        }
    }
}
