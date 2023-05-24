package com.project.BookingFlight.controller;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.model.dto.FlightDTO;
import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/getAllFlights")
    public ResponseEntity<List<FlightDTO>> getAllFlights(){
            return ResponseEntity.ok(flightService.getAllFlights());
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<FlightDTO> createFlight(@RequestBody Flight flight){
        return ResponseEntity.ok(flightService.saveNewFlight(flight));
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable(value = "id") Long id, @RequestBody Flight flight){
        flightService.updateFlight(id, flight);
        return ResponseEntity.status(200).build();
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FlightDTO> deleteFlight(@PathVariable(value = "id") Long id){
        try {
            flightService.deleteFlight(id);
            return ResponseEntity.status(200).build();
        }catch (GeneralException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PreAuthorize(value = "hasAnyRole('TRAVELLER')")
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date flightDate,
            @RequestParam(required = false) String airlineCode
    ) {
        try {
            List<FlightDTO> flights = flightService.findFlightByOriginOrDestinationOrDepartureDateOrAirlineCode(origin, destination, flightDate, airlineCode);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }catch (GeneralException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
