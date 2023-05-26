package com.project.BookingFlight.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private List<BookingFlightDTO> bookingFlights;
    private Boolean isCancelled;
    private String cancellationReason;
    private Boolean awaitingCancellation;

}
