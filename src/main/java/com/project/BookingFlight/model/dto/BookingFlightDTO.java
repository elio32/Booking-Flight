package com.project.BookingFlight.model.dto;


import com.project.BookingFlight.model.enums.BookingClassesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingFlightDTO {
    private Long id;
    private FlightDTO flight;
    private BookingClassesEnum bookingClass;
}
