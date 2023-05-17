package com.project.BookingFlight.mapper;

import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.UserApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMapper extends AbstractMapper<UserApp, UserDTO>{

    @Override
    public UserApp toEntity(UserDTO userDTO) {
        if (userDTO ==null){
            return null;
        }
        log.info("Converting UserDTO to Entity");
        UserApp user = new UserApp();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setRole(userDTO.getRole());
        return user;
    }

    @Override
    public UserDTO toDto(UserApp user) {
        if (user ==null){
            return null;
        }
        log.info("Converting User to DTO");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
