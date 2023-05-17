package com.project.BookingFlight.service;

import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.UserApp;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserDTO> getAllUsers();//ne kllapa duhet pagination
    UserDTO getUserByEmail(String email);
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserApp requestedUser);
    UserDTO saveUser(UserApp user);
    void deleteUser(Long id);

}
