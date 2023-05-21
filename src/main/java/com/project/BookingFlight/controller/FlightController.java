package com.project.BookingFlight.controller;

import com.project.BookingFlight.model.dto.FlightDTO;
import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<FlightDTO> createFlight(@RequestBody Flight flight){
        return ResponseEntity.ok(flightService.saveNewFlight(flight));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable(value = "id") Long id, @RequestBody Flight flight){
        flightService.updateFlight(id, flight);
        return ResponseEntity.status(200).build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FlightDTO> deleteFlight(@PathVariable(value = "id") Long id){
        flightService.deleteFlight(id);
        return ResponseEntity.status(200).build();
    }
    @GetMapping // se di per pjesen e IF AND ELSE
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam(value = "origin", required = false) String origin,
            @RequestParam(value = "destination", required = false) String destination,
            @RequestParam(value = "departureDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date departureDate,
            @RequestParam(value = "airlineCode", required = false) String airlineCode) {

        List<FlightDTO> flights = flightService.findFlightByOriginOrDestinationOrDepartureDateOrAirlineCode(
                origin, destination, departureDate, airlineCode);

        if (flights.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(flights);
        }
    }
}
