package com.project.BookingFlight.model.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;


@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isCanceled")
    private Boolean isCancelled;

    @Column(name = "awaitingCancellation")
    private Boolean awaitingCancellation;

    @Column(name = "cancellationReason")
    private String cancellationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @ToString.Exclude
    private UserApp user;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BookingFlight> bookingFlights;

}
