package com.project.BookingFlight.controller;

import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.UserApp;
import com.project.BookingFlight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping("/saveNewUser")
    public ResponseEntity<UserDTO> saveNewUser(@RequestBody UserApp user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(200).build();
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long id,@RequestBody UserApp user){
          userService.updateUser(id, user);
        return ResponseEntity.status(200).build();
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/getUser/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable(value = "email") String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/getAllByFlight/{flightId}")
    public ResponseEntity<List<UserDTO>> getAllUsersByFlight(@PathVariable(value = "flightId") Long flightId){
        return ResponseEntity.ok(userService.getAllTravellersByFlight(flightId));
    }

}
