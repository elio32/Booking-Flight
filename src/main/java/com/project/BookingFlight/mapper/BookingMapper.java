package com.project.BookingFlight.mapper;

import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingMapper extends AbstractMapper<Booking, BookingDTO>{

    @Override
    public Booking toEntity(BookingDTO bookingDTO) {
        if (bookingDTO ==null){
            return null;
        }
        log.info("Converting BookingDTO to Entity");
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setIsCancelled(bookingDTO.getIsCancelled());
        booking.setAwaitingCancellation(bookingDTO.getAwaitingCancellation());
        booking.setCancellationReason(bookingDTO.getCancellationReason());
        return booking;

    }

    @Override
    public BookingDTO toDto(Booking booking) {
        if (booking ==null){
            return null;
        }
        log.info("Converting Booking to DTO");
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setFlights(booking.getFlights());
        bookingDTO.setIsCancelled(booking.getIsCancelled());
        bookingDTO.setAwaitingCancellation(booking.getAwaitingCancellation());
        bookingDTO.setCancellationReason(booking.getCancellationReason());
        return bookingDTO;
    }
}
