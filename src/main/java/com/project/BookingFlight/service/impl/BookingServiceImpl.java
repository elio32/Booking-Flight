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
        log.info("Fetching booking with ID {} from DB", bookingId);
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        checkIfBookingExist(existingBooking);
        Booking booking = existingBooking.get();

        if (booking.getIsCancelled()) {
            log.error("Booking with ID {} is already cancelled", bookingId);
            throw new GeneralException("Booking has already been cancelled.");
        }
        booking.setAwaitingCancellation(true);

        log.info("Saving booking with ID {} as awaiting cancellation", bookingId);
        bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDTO confirmBookingCancellation(Long bookingId, BookingDTO bookingDTO) {
        log.info("Fetching booking with ID {} from DB", bookingId);
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        checkIfBookingExist(existingBooking);
        Booking booking = existingBooking.get();

        if (bookingDTO.getIsCancelled() && booking.getIsCancelled()) {
            log.error("Booking with ID {} is already cancelled", bookingId);
            throw new GeneralException("Booking is already cancelled.");
        }

        if (bookingDTO.getIsCancelled() != null) {
            // Update booking status to "Cancelled" if isCancelled is true
            booking.setIsCancelled(bookingDTO.getIsCancelled());

            if (bookingDTO.getIsCancelled() && booking.getAwaitingCancellation()) {
                // Admin approves the cancellation request
                booking.setAwaitingCancellation(false);

                // Set cancellation reason to null if isCancelled is true
                booking.setCancellationReason(null);
            } else if (!bookingDTO.getIsCancelled() && booking.getIsCancelled()) {
                // Admin changes the cancellation request from declined to accepted
                booking.setCancellationReason(null);
            }
        }
        log.info("Saving updated booking with ID {} to DB", bookingId);
        booking = bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDTO> getUserBookings(Long userId, Pagination pagination) {
        if (userId == null) {
            log.error("User ID is required.");
            throw new GeneralException("User ID is required.");
        }

        if (pagination == null) {
            pagination = new Pagination();
        }

        log.info("Fetching all bookings for user with ID {} with pagination {}", userId, pagination);
        Pageable pageable = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(),
                pagination.getSortByAscendingOrder() ? Sort.by(pagination.getSortByProperty()).ascending()
                        : Sort.by(pagination.getSortByProperty()).descending());
        Page<Booking> bookingPage = bookingRepository.findByUserId(userId, pageable);
        return bookingPage.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingDTO createBooking(Booking booking, String email) {

        if (booking.getBookingFlights() == null || booking.getBookingFlights().size() < 1) {
            log.error("There should be one flight included in the booking");
            throw new GeneralException("At least one flight should be included in the booking.");
        }

        log.info("Fetching the user from DB to create the flight");
        UserApp user =userRepository.findByEmail(email).
                orElseThrow(() -> new GeneralException("No user found with email: " + email));

        booking.setUser(user);

        for (BookingFlight bookingFlight : booking.getBookingFlights()) {
            Optional<Flight> flight = flightRepository.findById(bookingFlight.getFlight().getId());
            if (flight.isEmpty()) {
                log.error("Flight with ID {} does not exist", bookingFlight.getId());
                throw new GeneralException("Flight with ID " + bookingFlight.getId() + " does not exist.");
            }
            // Check if the flight has already departed
            else if (flight.get().getDepartureDate().before(new Date())){
                log.error("Flight {} has already departed", flight.get().getFlightNumber());
                throw new GeneralException("Flight " + flight.get().getFlightNumber() + " has already departed.");
            }

            // Check if there are enough seats available in the requested booking class
            int availableSeats = getAvailableSeats(flight.get(), bookingFlight.getBookingClass());
            if (availableSeats <= 0) {
                log.error("Not enough seats available in the requested booking class for flight {}",
                        flight.get().getFlightNumber());
                throw new GeneralException("Not enough seats available in the requested booking class for flight "
                        + flight.get().getFlightNumber());
            } else {
                int availableClassSeats = getAvailableSeats(flight.get(), bookingFlight.getBookingClass());
                if (availableClassSeats <= 0) {
                    log.error("Not enough seats available in the requested booking class for flight {}",
                            flight.get().getFlightNumber());
                    throw new GeneralException("Not enough seats available in the requested booking class for flight "
                            + flight.get().getFlightNumber());
                } else {
                    // Decrease the available seats count by 1 for the requested booking class
                    decreaseAvailableSeats(flight.get(), bookingFlight.getBookingClass());
                }

                bookingFlight.setBooking(booking);
            }
            bookingFlight.setBooking(booking);
        }

        booking.setIsCancelled(false);
        booking.setAwaitingCancellation(false);
        booking.setCancellationReason(null);

        booking = bookingRepository.save(booking);
        Booking returnedBooking = bookingRepository.findById(booking.getId()).get();
        return bookingMapper.toDto(returnedBooking);
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

    private void decreaseAvailableSeats(Flight flight, BookingClassesEnum bookingClass) {
        List<BookingFlight> bookingFlights = flight.getBookingFlights();

        for (BookingFlight bookingFlight : bookingFlights) {
            if (bookingFlight.getBookingClass().equals(bookingClass)) {
                int availableSeats = 0;

                switch (bookingClass) {
                    case BUSINESS -> {
                        availableSeats = flight.getAvailableBusinessSeats();
                        if (availableSeats > 0) {
                            flight.setAvailableBusinessSeats(availableSeats - 1);
                        }
                    }
                    case ECONOMY -> {
                        availableSeats = flight.getAvailableEconomySeats();
                        if (availableSeats > 0) {
                            flight.setAvailableEconomySeats(availableSeats - 1);
                        }
                    }
                    case FIRST -> {
                        availableSeats = flight.getAvailableFirstClassSeats();
                        if (availableSeats > 0) {
                            flight.setAvailableFirstClassSeats(availableSeats - 1);
                        }
                    }
                    case PREMIUM_ECONOMY -> {
                        availableSeats = flight.getAvailablePremiumEconomySeats();
                        if (availableSeats > 0) {
                            flight.setAvailablePremiumEconomySeats(availableSeats - 1);
                        }
                    }
                    default -> throw new GeneralException("Invalid booking class: " + bookingClass);
                }

                if (availableSeats <= 0) {
                    throw new GeneralException("Not enough available seats in the requested booking class for flight: "
                            + flight.getFlightNumber());
                }

                flightRepository.save(flight);
                break;
            }
        }
    }


    private void checkIfBookingExist(Optional<Booking> booking) {
        log.info("Checking if booking exists");
        if (booking.isEmpty()) {
            log.error("Booking not found");
            throw new GeneralException("Booking not found");
        }
    }

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
