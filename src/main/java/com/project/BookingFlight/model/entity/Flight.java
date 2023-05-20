package com.project.BookingFlight.model.entity;

import com.project.BookingFlight.model.enums.BookingClassesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
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
    @Column(name = "totalSeats",nullable = false)
    private Integer TotalSeats;
    @Column(name = "classes",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BookingClassesEnum bookingClasses;

    @ManyToMany(mappedBy = "flights",cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
