package com.project.BookingFlight.model.dto;

import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.model.enums.BookingClassesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingFlightDTO {
    private Long id;
    private Booking booking;
    private Flight flight;
    private BookingClassesEnum bookingClass;
}
