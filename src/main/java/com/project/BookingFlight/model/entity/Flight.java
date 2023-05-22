package com.project.BookingFlight.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "airlineCode",nullable = false)
    private String airlineCode;
    @Column(name = "flightNumber",nullable = false,unique = true)
    private String flightNumber;
    @Column(name = "origin",nullable = false)
    private String origin;
    @Column(name = "destination",nullable = false)
    private String destination;
    @Column(name = "departuresDate",nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date departureDate;
    @Column(name = "arrivaldATE",nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date arrivalDate;
    @Column(name = "departureTime",nullable = false)
    private LocalTime departureTime;
    @Column(name = "arrivalTime",nullable = false)
    private LocalTime arrivalTime;
    @Column(name = "price",nullable = false)
    private Double price;
    @Column(name = "totalEconomySeats",nullable = false)
    private Integer totalEconomySeats;
    @Column(name = "totalPremiumEconomySeats",nullable = false)
    private Integer totalPremiumEconomySeats;
    @Column(name = "totalBusinessSeats",nullable = false)
    private Integer totalBusinessSeats;
    @Column(name = "totalFirstClassSeats",nullable = false)
    private Integer totalFirstClassSeats;
    @Column(name = "availableEconomySeats",nullable = false)
    private Integer availableEconomySeats;
    @Column(name = "availablePremiumEconomySeats",nullable = false)
    private Integer availablePremiumEconomySeats;
    @Column(name = "availableBusinessSeats",nullable = false)
    private Integer availableBusinessSeats;
    @Column(name = "availableFirstClassSeats",nullable = false)
    private Integer availableFirstClassSeats;

    @ManyToMany(mappedBy = "flights",cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Flight(String airlineCode, String flightNumber, String origin, String destination, Date departureDate, Date arrivalDate, LocalTime departureTime, LocalTime arrivalTime, Double price, Integer totalEconomySeats, Integer totalPremiumEconomySeats, Integer totalBusinessSeats, Integer totalFirstClassSeats, List<Booking> bookings) {
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.totalEconomySeats = totalEconomySeats;
        this.totalPremiumEconomySeats = totalPremiumEconomySeats;
        this.totalBusinessSeats = totalBusinessSeats;
        this.totalFirstClassSeats = totalFirstClassSeats;
        this.availableEconomySeats = totalEconomySeats;
        this.availablePremiumEconomySeats = totalPremiumEconomySeats;
        this.availableBusinessSeats = totalBusinessSeats;
        this.availableFirstClassSeats = totalFirstClassSeats;
        if (bookings == null) this.bookings = new ArrayList<>();
        else this.bookings = bookings;
    }

}
