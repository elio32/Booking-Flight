package com.project.BookingFlight.controller;

import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.UserApp;
import com.project.BookingFlight.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    private UserServiceImpl userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/saveNewUser")
    public ResponseEntity<UserDTO> saveNewUser(@RequestBody UserApp user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long id,@RequestBody UserApp user){
          userService.updateUser(id, user);
        return ResponseEntity.status(200).build();
    }

}
