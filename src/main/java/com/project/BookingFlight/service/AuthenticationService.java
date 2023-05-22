package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.AuthenticationRequest;
import com.project.BookingFlight.model.dto.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

}
