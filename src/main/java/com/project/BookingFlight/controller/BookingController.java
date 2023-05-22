package com.project.BookingFlight.controller;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.model.dto.BookingDTO;
import com.project.BookingFlight.model.entity.Pagination;
import com.project.BookingFlight.model.enums.UserRoleEnum;
import com.project.BookingFlight.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/traveller/{travellerId}")
    public ResponseEntity<List<BookingDTO>> getAllBookingsForTraveller(@PathVariable(value = "travellerId") Long travellerId){
        return ResponseEntity.ok(bookingService.getAllBookingsByTraveller(travellerId));
    }

    @PostMapping("/{bookingId}/cancel") // se di sa e sakt eshte
    public ResponseEntity<BookingDTO> requestBookingCancellation(
            @PathVariable Long bookingId,
            @RequestParam(required = false) String declineReason,
            @RequestParam(required = false) UserRoleEnum userRole
    ) {
        try {
            BookingDTO bookingDTO = bookingService.requestBookingCancellation(bookingId, declineReason, userRole);
            return ResponseEntity.ok(bookingDTO);
        } catch (GeneralException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/getBookingsForUser/{id}") // shife
    public ResponseEntity<List<BookingDTO>> getBookingsForUser(@PathVariable(value = "id") Long id,@RequestBody(required = false) Pagination pagination){
        try {
            List<BookingDTO> bookings = bookingService.getUserBookings(id, pagination);
            return ResponseEntity.ok(bookings);
        }catch (GeneralException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
