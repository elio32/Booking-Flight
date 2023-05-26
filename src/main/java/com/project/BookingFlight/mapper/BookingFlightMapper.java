package com.project.BookingFlight.mapper;

import com.project.BookingFlight.model.dto.BookingFlightDTO;
import com.project.BookingFlight.model.entity.BookingFlight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingFlightMapper extends AbstractMapper<BookingFlight, BookingFlightDTO>{

    private final FlightMapper flightMapper;
    @Override
    public BookingFlight toEntity(BookingFlightDTO bookingFlightDTO) {
        return null;
    }

    @Override
    public BookingFlightDTO toDto(BookingFlight bookingFlight) {
        if (bookingFlight ==null){
            return null;
        }
        BookingFlightDTO bookingFlightDTO = new BookingFlightDTO();
        bookingFlightDTO.setId(bookingFlight.getId());
//        bookingFlightDTO.setBooking(bookingFlight.getBooking());
        bookingFlightDTO.setFlight(flightMapper.toDto(bookingFlight.getFlight()));
        bookingFlightDTO.setBookingClass(bookingFlight.getBookingClass());
        return bookingFlightDTO;
    }
}
