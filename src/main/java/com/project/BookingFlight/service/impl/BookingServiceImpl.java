package com.project.BookingFlight.service.impl;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.mapper.BookingMapper;
import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.UserApp;
import com.project.BookingFlight.repository.BookingRepository;
import com.project.BookingFlight.repository.UserRepository;
import com.project.BookingFlight.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private BookingMapper bookingMapper;

    @Override
    public List<BookingDTO> getAllBookingsByTraveller(Long travellerId) {
        Optional<UserApp> traveller = userRepository.findById(travellerId);
        checkIfUserExist(traveller);
        List<Booking> bookings = bookingRepository.findBookingsByUser(traveller.get());
        return bookings.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteBooking(Long id) {

    }

    @Override
    public BookingDTO findBookingById(Long id) {
        return null;
    }

    @Override
    public BookingDTO saveNewBooking(Booking booking) {
        return null;
    }

    @Override
    public List<BookingDTO> showAllBookings(Long id) {
        return null;
    }

    @Override
    public BookingDTO saveNewBookingForUser(Long userId, Booking booking) {
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
