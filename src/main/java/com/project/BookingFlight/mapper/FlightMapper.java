package com.project.BookingFlight.mapper;

import com.project.BookingFlight.model.dto.FlightDTO;
import com.project.BookingFlight.model.entity.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlightMapper extends AbstractMapper<Flight, FlightDTO>{
    @Override
    public Flight toEntity(FlightDTO flightDTO) {
        if (flightDTO ==null){
            return null;
        }
        log.info("Converting FlightDTO to Entity");
        Flight flight = new Flight();
        flight.setId(flight.getId());
        flight.setAirlineCode(flightDTO.getAirlineCode());
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        flight.setDepartureDate(flightDTO.getDepartureDate());
        flight.setArrivalDate(flightDTO.getArrivalDate());
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setArrivalTime(flightDTO.getArrivalTime());
        flight.setPrice(flightDTO.getPrice());
        flight.setTotalSeats(flightDTO.getTotalSeats());
        flight.setBookingClasses(flightDTO.getBookingClasses());
        return flight;
    }

    @Override
    public FlightDTO toDto(Flight flight) {
        if (flight ==null){
            return null;
        }
        log.info("Converting Flight to DTO");
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(flight.getId());
        flightDTO.setAirlineCode(flight.getAirlineCode());
        flightDTO.setFlightNumber(flight.getFlightNumber());
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setDepartureDate(flight.getDepartureDate());
        flightDTO.setArrivalDate(flight.getArrivalDate());
        flightDTO.setDepartureTime(flight.getDepartureTime());
        flightDTO.setArrivalTime(flight.getArrivalTime());
        flightDTO.setBookingClasses(flight.getBookingClasses());
        return flightDTO;
    }
}
