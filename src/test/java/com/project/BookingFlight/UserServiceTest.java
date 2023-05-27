package com.project.BookingFlight;

import com.project.BookingFlight.mapper.UserMapper;
import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.UserApp;
import com.project.BookingFlight.repository.UserRepository;
import com.project.BookingFlight.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {

        UserApp user1 = new UserApp();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");

        UserApp user2 = new UserApp();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        List<UserApp> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        // Set up mock behavior
        when(userRepository.findAll()).thenReturn(userList);

        UserDTO userDto1 = new UserDTO();
        userDto1.setId(1L);
        userDto1.setUsername("user1");
        userDto1.setEmail("user1@example.com");

        UserDTO userDto2 = new UserDTO();
        userDto2.setId(2L);
        userDto2.setUsername("user2");
        userDto2.setEmail("user2@example.com");

        List<UserDTO> expectedUserDtoList = new ArrayList<>();
        expectedUserDtoList.add(userDto1);
        expectedUserDtoList.add(userDto2);

        when(userMapper.toDto(user1)).thenReturn(userDto1);
        when(userMapper.toDto(user2)).thenReturn(userDto2);

        // Call the method to get all users
        List<UserDTO> actualUserDtoList = userService.getAllUsers();

        // Verify the result
        assertEquals(expectedUserDtoList.size(), actualUserDtoList.size());

    }
}
