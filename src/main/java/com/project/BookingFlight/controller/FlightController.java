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
        try {
            return ResponseEntity.ok(flightService.getAllFlights());
        }catch (GeneralException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<FlightDTO> createFlight(@RequestBody Flight flight){
        try {
            return ResponseEntity.ok(flightService.saveNewFlight(flight));
        }catch (GeneralException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable(value = "id") Long id, @RequestBody Flight flight){
        try {
            flightService.updateFlight(id, flight);
            return ResponseEntity.status(200).build();
        }catch (GeneralException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FlightDTO> deleteFlight(@PathVariable(value = "id") Long id){
        try {
            flightService.deleteFlight(id);
            return ResponseEntity.status(200).build();
        }catch (GeneralException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PreAuthorize(value = "hasAnyRole('TRAVELLER')")
    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date flightDate,
            @RequestParam(required = false) String airlineCode
    ) {
        try {
            List<FlightDTO> flights = flightService.findFlightByOriginOrDestinationOrDepartureDateOrAirlineCode
                    (origin, destination, flightDate, airlineCode);
            return  ResponseEntity.ok(flights);
        }catch (GeneralException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
