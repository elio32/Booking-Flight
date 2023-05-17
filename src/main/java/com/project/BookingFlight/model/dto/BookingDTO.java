package com.project.BookingFlight.model.dto;

import com.project.BookingFlight.model.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private List<Flight> flights;
    private boolean isCancelled;
    private String cancellationReason;
}
