package com.project.BookingFlight.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private Long id;
    private String airlineCode;
    private String flightNumber;
    private String origin;
    private String destination;
    private Date departureDate;
    private Date arrivalDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Double price;
    private Integer totalEconomySeats;
    private Integer totalPremiumEconomySeats;
    private Integer totalBusinessSeats;
    private Integer totalFirstClassSeats;
    private Integer availableEconomySeats;
    private Integer availablePremiumEconomySeats;
    private Integer availableBusinessSeats;
    private Integer availableFirstClassSeats;
}
