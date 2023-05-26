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
    public void deleteFlight(Long id) {
        log.info("Fetching flight with id {} from DB" ,id);
        Flight flight = flightRepository.findById(id).
                orElseThrow(() -> new GeneralException("Flight with this id doesnt exist" + id));

        checkIfExist(Optional.ofNullable(flight));
        //checks if flights has bookings
        List<Booking> bookings = bookingRepository.findBookingByBookingFlightsFlight(flight);
        if (!bookings.isEmpty()) {
            throw new GeneralException("Cannot delete flight with this id as it has already been booked");
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
            throw new GeneralException("Flight with this flightNumber already exists ");
        }
        log.info("Saving new flight{} to DB",flight.getFlightNumber());
        flight = flightRepository.save(flight);
        return flightMapper.toDto(flight);
    }

    @Override// kontrolloje
    public List<FlightDTO> findFlightByOriginOrDestinationOrDepartureDateOrAirlineCode(String origin, String destination,
                                                                                       Date departureDate, String airlineCode) {
        log.info("Searching for a flight by Origin {} or Destination {} or " +
                "Departure Date {} or Airline Code {}",origin,destination,departureDate,airlineCode);
        Date currentDate = new Date();
        if (departureDate.before(currentDate)) {
            throw new GeneralException("Flight date cannot be in the past");
        }

        if (airlineCode != null && !airlineCode.isEmpty()) {
            List<Flight> flights = flightRepository.findByOriginAndDestinationAndDepartureDateAndAirlineCode
                    (origin, destination, departureDate, airlineCode);
            return flights.stream().map(flightMapper::toDto).collect(Collectors.toList());
        } else {
            List<Flight> flightList = flightRepository.findByOriginAndDestinationAndDepartureDate
                    (origin, destination, departureDate);
            return flightList.stream().map(flightMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override // ben update flighted qe nuk jan ber booked, por ato qe jan ber booked nuk i ndryshon, por sme jep error, me jep status 200.
    public FlightDTO updateFlight(Long id, Flight requestedFlight) {
        Flight existingFlight = flightRepository.findById(id).orElseThrow(() -> new GeneralException("Flight not found"));
        log.info("Updating flight {}", existingFlight.getFlightNumber());

        if (hasTravelerBookings(existingFlight)) {
            // If the flight has bookings, only update the departure time
            if (requestedFlight.getDepartureTime() != null) {
                existingFlight.setDepartureTime(requestedFlight.getDepartureTime());
            } else {
                throw new GeneralException("Flight has bookings, only departure time can be updated.");
            }
        } else {

            existingFlight.setFlightNumber(requestedFlight.getFlightNumber());
            existingFlight.setAirlineCode(requestedFlight.getAirlineCode());
            existingFlight.setOrigin(requestedFlight.getOrigin());
            existingFlight.setDestination(requestedFlight.getDestination());
            existingFlight.setDepartureDate(requestedFlight.getDepartureDate());
            existingFlight.setArrivalDate(requestedFlight.getArrivalDate());
            existingFlight.setDepartureTime(requestedFlight.getDepartureTime());
            existingFlight.setArrivalTime(requestedFlight.getArrivalTime());
            existingFlight.setPrice(requestedFlight.getPrice());
            existingFlight.setTotalEconomySeats(requestedFlight.getTotalEconomySeats());
            existingFlight.setTotalPremiumEconomySeats(requestedFlight.getTotalPremiumEconomySeats());
            existingFlight.setTotalBusinessSeats(requestedFlight.getTotalBusinessSeats());
            existingFlight.setTotalFirstClassSeats(requestedFlight.getTotalFirstClassSeats());
            existingFlight.setAvailableEconomySeats(requestedFlight.getAvailableEconomySeats());
            existingFlight.setAvailablePremiumEconomySeats(requestedFlight.getAvailablePremiumEconomySeats());
            existingFlight.setAvailableBusinessSeats(requestedFlight.getAvailableBusinessSeats());
            existingFlight.setAvailableFirstClassSeats(requestedFlight.getAvailableFirstClassSeats());
            existingFlight.setBookingFlights(requestedFlight.getBookingFlights());
        }

        Flight updatedFlight = flightRepository.save(existingFlight);
        return flightMapper.toDto(updatedFlight);
    }

    private void checkIfExist(Optional<Flight> flight) {
        log.info("Checking if flight exists");
        if (flight.isEmpty()) {
            log.error("Flight not found");
            throw new GeneralException("Flight not found");
        }
    }

    private boolean hasTravelerBookings(Flight flight) {
        return !flight.getBookingFlights().isEmpty();
    }

}
