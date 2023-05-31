package com.project.BookingFlight.controller;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.model.dto.AuthenticationRequest;
import com.project.BookingFlight.model.dto.AuthenticationResponse;
import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.service.UserService;
import com.project.BookingFlight.service.impl.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationServiceImpl authenticationService,UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST,path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        }catch (GeneralException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO user){
        try {
            return ResponseEntity.ok(userService.register(user));
        }catch (GeneralException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
