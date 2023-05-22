package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.UserApp;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserByEmail(String email);
    UserDTO updateUser(Long id, UserApp requestedUser);
    UserDTO saveUser(UserApp user);
    void deleteUser(Long id);
    List<UserDTO> getAllTravellersByFlight(Long flightId);
    UserDTO register(UserDTO userDTO);

}
