package com.project.BookingFlight.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.BookingFlight.model.enums.BookingClassesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking_flight")
public class BookingFlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnore
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name ="booking_class" ,nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BookingClassesEnum bookingClass;
}
