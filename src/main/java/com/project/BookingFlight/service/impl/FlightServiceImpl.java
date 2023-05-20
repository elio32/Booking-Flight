package com.project.BookingFlight.service.impl;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.mapper.FlightMapper;
import com.project.BookingFlight.model.dto.FlightDTO;
import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.repository.BookingRepository;
import com.project.BookingFlight.repository.FlightRepository;
import com.project.BookingFlight.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private FlightRepository flightRepository;
    private FlightMapper flightMapper;
    private BookingRepository bookingRepository;
    @Override
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream().map(flightMapper::toDto).collect(Collectors.toList());
    }

    //delete a flight if it has no bookings
    @Override
    public void deleteFlight(Long id) { // kontrolloje si method
           //Check if flight exists
        log.info("Fetching flight with id {} from DB" ,id);
        Optional<Flight> flight = flightRepository.findById(id);
        checkIfExist(flight);
        //checks if flights has bookings
           List<Booking> bookings = bookingRepository.findBookingByFlights(flight); // tek repo e (Optional<Flight> flight) ??????
           if (!bookings.isEmpty()) {
               throw new GeneralException("Cannot delete flight with this id as it has already been booked", null); //Arrays.asList(flight.getid) nuk me nxjerr flight.getId
           }
           log.info("Deleting flight with id{}",id);
           flightRepository.deleteById(id); //id duhet flight???
    }

    @Override
    public FlightDTO saveNewFlight(Flight flight) {
        log.info("Checking if flight exist");
        Optional<Flight> existingFlight = flightRepository.findByFlightNumber(flight.getFlightNumber());
        if (existingFlight.isPresent()){
            log.error("Flight with flightNumber {} already exists",flight.getFlightNumber());
            throw new GeneralException("Flight with this flightNumber already exists ", Arrays.asList(flightMapper.toDto(existingFlight.get()))); // pse duhet ber convert to Dto?
        }
        log.info("Saving new flight{} to DB",flight.getFlightNumber());
        flight = flightRepository.save(flight);
        return flightMapper.toDto(flight);
    }

    @Override
    public FlightDTO findFlightByOriginOrDestinationOrDepartureDateOrAirlineCode(String origin, String destination, Date departureDate, String airlineCode) {
        log.info("Searching for a flight by Origin {} or Destination {} or Departure Date {} or Airline Code {}",origin,destination,departureDate,airlineCode);
        if (airlineCode != null && !airlineCode.isEmpty()) {
            // Search for flights with the specified airline code
            Flight flight= flightRepository.findByOriginAndDestinationAndDepartureDateAndAirlineCode(
                    origin, destination, departureDate, airlineCode);
            return flightMapper.toDto(flight);
        } else {
            // Search for flights from all airlines
            Flight flight =flightRepository.findByOriginAndDestinationAndDepartureDate(
                    origin, destination, departureDate);
            return flightMapper.toDto(flight);
        }
    }

    @Override
    public FlightDTO updateFlight(Long id, Flight requestedFlight) {
        Optional<Flight> existingFlight = flightRepository.findById(id);
        checkIfExist(existingFlight);
        Flight flight = existingFlight.get();
        log.info("Updating flight{}", flight.getFlightNumber());

        // Check if any traveler has booked the flight
        if (hasTravelerBookings(flight)) {
            // If the flight has bookings, only update the departure time
            if (requestedFlight.getDepartureTime() != null) {
                flight.setDepartureTime(requestedFlight.getDepartureTime());
            } else {
                throw new GeneralException("Flight has bookings, only departure time can be updated.", null);
            }
        } else {
            // If the flight has no bookings, update all fields
            if (requestedFlight.getFlightNumber() != null) {
                // Check if the requested flight number is already taken by another flight
                if (flightRepository.findByFlightNumber(requestedFlight.getFlightNumber()).isPresent()) {
                    throw new GeneralException("Flight number is already taken!",
                            Arrays.asList(requestedFlight.getFlightNumber()));
                }
                flight.setFlightNumber(requestedFlight.getFlightNumber());
            }
            if (requestedFlight.getAirlineCode() != null) {
                flight.setAirlineCode(requestedFlight.getAirlineCode());
            }
            if (requestedFlight.getOrigin() != null) {
                flight.setOrigin(requestedFlight.getOrigin());
            }
            if (requestedFlight.getDestination() != null) {
                flight.setDestination(requestedFlight.getDestination());
            }
            if (requestedFlight.getDepartureDate() != null) {
                flight.setDepartureDate(requestedFlight.getDepartureDate());
            }
            if (requestedFlight.getArrivalDate() != null) {
                flight.setArrivalDate(requestedFlight.getArrivalDate());
            }
            if (requestedFlight.getDepartureTime() != null) {
                flight.setDepartureTime(requestedFlight.getDepartureTime());
            }
            if (requestedFlight.getArrivalTime() != null) {
                flight.setArrivalTime(requestedFlight.getArrivalTime());
            }
            if (requestedFlight.getPrice() != null) {
                flight.setPrice(requestedFlight.getPrice());
            }
            if (requestedFlight.getTotalSeats() != null) {
                flight.setTotalSeats(requestedFlight.getTotalSeats());
            }
            if (requestedFlight.getBookingClasses() != null) {
                flight.setBookingClasses(requestedFlight.getBookingClasses());
            }
            if (requestedFlight.getBookings() != null) {
                flight.setBookings(requestedFlight.getBookings());
            }
        }
        flight = flightRepository.save(flight);
        return flightMapper.toDto(flight);
    }

    private void checkIfExist(Optional<Flight> flight) {
        log.info("Checking if flight exists");
        if (flight.isEmpty()) {
            log.error("Flight not found");
            throw new GeneralException("Flight not found", null);
        }
    }

    private boolean hasTravelerBookings(Flight flight) {
        return !flight.getBookings().isEmpty();
    }

}