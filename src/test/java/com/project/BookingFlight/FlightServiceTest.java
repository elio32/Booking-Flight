package com.project.BookingFlight;

import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.repository.FlightRepository;
import com.project.BookingFlight.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeleteFlight_WhenFlightExists_ShouldDeleteFlight() {
        long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);

        // Set up mock behavior
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        flightService.deleteFlight(flightId);

        // Verify that the flight has been deleted
        verify(flightRepository, times(1)).deleteById(flightId);
    }
}
