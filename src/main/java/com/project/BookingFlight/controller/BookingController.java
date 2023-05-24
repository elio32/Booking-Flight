package com.project.BookingFlight.controller;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.BookingFlight;
import com.project.BookingFlight.model.entity.Pagination;
import com.project.BookingFlight.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping(path = "/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/traveller/{travellerId}")
    public ResponseEntity<List<BookingDTO>> getAllBookingsForTraveller(@PathVariable(value = "travellerId") Long travellerId){
        try {
            return ResponseEntity.ok(bookingService.getAllBookingsByTraveller(travellerId));
        }catch (GeneralException e){
            return  ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{bookingId}/cancel") // se di sa e sakt eshte
    public ResponseEntity<BookingDTO> requestBookingCancellation(@PathVariable(value = "bookingId") Long bookingId) {
        try {
            BookingDTO bookingDTO = bookingService.requestBookingCancellation(bookingId);
            return ResponseEntity.ok(bookingDTO);
        } catch (GeneralException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<BookingDTO> confirmBookingCancellation(
            @PathVariable("bookingId") Long bookingId,
            @RequestParam(value = "declineReason", required = false) String declineReason) {
        try {
            BookingDTO bookingDTO = bookingService.confirmBookingCancellation(bookingId, declineReason);
            return ResponseEntity.ok(bookingDTO);
        } catch (GeneralException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    //Can view all bookings of a specific traveller
    @GetMapping("/getBookingsForUser/{id}")
    public ResponseEntity<List<BookingDTO>> getBookingsForUser(@PathVariable(value = "id") Long id,@RequestBody(required = false) Pagination pagination){
        try {
            List<BookingDTO> bookings = bookingService.getUserBookings(id, pagination);
            return ResponseEntity.ok(bookings);
        }catch (GeneralException e){
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }
    //create booking
    @PostMapping("/createBooking") //kontrolloje
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingFlight request) {
        try {
            BookingDTO createdBooking = bookingService.createBooking(request.getFlight().getId(), request.getBookingClass()); // problemi qentron tek getId()
            return ResponseEntity.ok(createdBooking);
        } catch (GeneralException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }
}

