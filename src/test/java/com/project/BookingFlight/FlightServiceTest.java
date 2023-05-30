package com.project.BookingFlight;

import com.project.BookingFlight.mapper.FlightMapper;
import com.project.BookingFlight.model.dto.FlightDTO;
import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.repository.FlightRepository;
import com.project.BookingFlight.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveNewFlight_WhenFlightNumberIsUnique_ShouldSaveFlight() {

        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setFlightNumber("AA200");
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());

        when(flightRepository.findByFlightNumber(flightDTO.getFlightNumber())).thenReturn(Optional.empty());
        when(flightMapper.toDto(flight)).thenReturn(flightDTO);
        when(flightRepository.save(flight)).thenReturn(flight);

        FlightDTO savedFlightDTO = flightService.saveNewFlight(flight);

        assertNotNull(savedFlightDTO);
        assertEquals(flightDTO, savedFlightDTO);
    }
}
