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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final BookingRepository bookingRepository;
    @Override
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream().map(flightMapper::toDto).collect(Collectors.toList());
    }

    //delete a flight if it has no bookings
    @Override
    public void deleteFlight(Long id) { // kontrolloje si method
        log.info("Fetching flight with id {} from DB" ,id);
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new GeneralException());

        checkIfExist(flight);
        //checks if flights has bookings
           List<Booking> bookings = bookingRepository.findBookingByFlights(flight); // tek repo e (Optional<Flight> flight) ??????
           if (!bookings.isEmpty()) {
               throw new GeneralException("Cannot delete flight with this id as it has already been booked", null); //Arrays.asList(flight.getid) nuk me nxjerr flight.getId
           }
           log.info("Deleting flight with id{}",id);
           flightRepository.deleteById(id);
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
    public List<FlightDTO> findFlightByOriginOrDestinationOrDepartureDateOrAirlineCode(String origin, String destination, Date departureDate, String airlineCode) {
        log.info("Searching for a flight by Origin {} or Destination {} or Departure Date {} or Airline Code {}",origin,destination,departureDate,airlineCode);
        if (airlineCode != null && !airlineCode.isEmpty()) {
            List<Flight> flight= flightRepository.findByOriginAndDestinationAndDepartureDateAndAirlineCode(
                    origin, destination, departureDate, airlineCode);
            return flight.stream().map(flightMapper::toDto).collect(Collectors.toList());
        } else {
            List<Flight> flight =flightRepository.findByOriginAndDestinationAndDepartureDate(
                    origin, destination, departureDate);
            return flight.stream().map(flightMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override
    public FlightDTO updateFlight(Long id, Flight requestedFlight) {
        Flight existingFlight = flightRepository.findById(id).orElse(null);
        Flight flight = new Flight();
        if (checkIfExist(existingFlight)){
             flight = existingFlight;
        }
        log.info("Updating flight{}", flight.getFlightNumber());

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
            }

              flight = requestedFlight;
//            if (requestedFlight.getAirlineCode() != null) {
//                flight.setAirlineCode(requestedFlight.getAirlineCode());
//            }
//            if (requestedFlight.getOrigin() != null) {
//                flight.setOrigin(requestedFlight.getOrigin());
//            }
//            if (requestedFlight.getDestination() != null) {
//                flight.setDestination(requestedFlight.getDestination());
//            }
//            if (requestedFlight.getDepartureDate() != null) {
//                flight.setDepartureDate(requestedFlight.getDepartureDate());
//            }
//            if (requestedFlight.getArrivalDate() != null) {
//                flight.setArrivalDate(requestedFlight.getArrivalDate());
//            }
//            if (requestedFlight.getDepartureTime() != null) {
//                flight.setDepartureTime(requestedFlight.getDepartureTime());
//            }
//            if (requestedFlight.getArrivalTime() != null) {
//                flight.setArrivalTime(requestedFlight.getArrivalTime());
//            }
//            if (requestedFlight.getPrice() != null) {
//                flight.setPrice(requestedFlight.getPrice());
//            }
//            if (requestedFlight.getTotalEconomySeats() != null) {
//                flight.setTotalEconomySeats(requestedFlight.getTotalEconomySeats());
//            }
//            if (requestedFlight.getTotalPremiumEconomySeats() != null) {
//                flight.setTotalPremiumEconomySeats(requestedFlight.getTotalPremiumEconomySeats());
//            }
//            if (requestedFlight.getTotalBusinessSeats() != null) {
//                flight.setTotalBusinessSeats(requestedFlight.getTotalBusinessSeats());
//            }
//            if (requestedFlight.getTotalFirstClassSeats() != null) {
//                flight.setTotalFirstClassSeats(requestedFlight.getTotalFirstClassSeats());
//            }
//            if (requestedFlight.getAvailableEconomySeats() != null) {
//                flight.setAvailableEconomySeats(requestedFlight.getAvailableEconomySeats());
//            }
//            if (requestedFlight.getAvailablePremiumEconomySeats() != null) {
//                flight.setAvailablePremiumEconomySeats(requestedFlight.getAvailablePremiumEconomySeats());
//            }
//            if (requestedFlight.getAvailableBusinessSeats() != null) {
//                flight.setAvailableBusinessSeats(requestedFlight.getAvailableBusinessSeats());
//            }
//            if (requestedFlight.getAvailableFirstClassSeats() != null) {
//                flight.setAvailableFirstClassSeats(requestedFlight.getAvailableFirstClassSeats());
//            }
//            if (requestedFlight.getBookings() != null) {
//                flight.setBookings(requestedFlight.getBookings());
//            }
        }
        try {
            flight = flightRepository.save(flight);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return flightMapper.toDto(flight);
    }

    private boolean checkIfExist(Flight flight) {
        log.info("Checking if flight exists");
        if (flight == null) {
            log.error("Flight not found");
            throw new GeneralException("Flight not found", null);
        }
        return true;
    }


    private boolean hasTravelerBookings(Flight flight) {
        return !flight.getBookings().isEmpty();
    }

}
