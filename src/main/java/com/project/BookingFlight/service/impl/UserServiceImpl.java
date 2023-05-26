package com.project.BookingFlight.service.impl;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.mapper.UserMapper;
import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.Booking;
import com.project.BookingFlight.model.entity.Flight;
import com.project.BookingFlight.model.entity.UserApp;
import com.project.BookingFlight.model.enums.UserRoleEnum;
import com.project.BookingFlight.repository.BookingRepository;
import com.project.BookingFlight.repository.FlightRepository;
import com.project.BookingFlight.repository.UserRepository;
import com.project.BookingFlight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.info("Fetching User{} from DB",email);
        Optional<UserApp> user = userRepository.findByEmail(email);
        checkIfUserExist(user);
        return userMapper.toDto(user.get());

    }

    @Override
    public UserDTO updateUser(Long id, UserApp requestedUser) {
        Optional<UserApp> existingUser = userRepository.findById(id);
        checkIfUserExist(existingUser);
        UserApp user = existingUser.get();
        log.info("Updating user{}",user.getUsername());
        if(requestedUser.getUsername() != null) {
            if(userRepository.findByUsername(requestedUser.getUsername()).isPresent()) {
                throw new GeneralException("Username is taken!");
            }
            user.setUsername(requestedUser.getUsername());
        }
        user.setPassword(existingUser.get().getPassword());
        if (requestedUser.getFirstName() != null) {
            user.setFirstName(requestedUser.getFirstName());}
        if (requestedUser.getLastName() != null){
            user.setLastName(requestedUser.getLastName());}
        if (requestedUser.getEmail() != null) {
            user.setEmail(requestedUser.getEmail());}
        if (requestedUser.getPhoneNumber() != null) {
            user.setPhoneNumber(requestedUser.getPhoneNumber());}
        if (requestedUser.getAddress() != null) {
            user.setAddress(requestedUser.getAddress());}
        if (requestedUser.getRole() != null){
            user.setRole(requestedUser.getRole());}
        user = userRepository.save(user);
        return userMapper.toDto(user);

    }

    @Override
    public UserDTO saveUser(UserApp user) {
        log.info("Saving new user{} to DB",user.getUsername());
            user.setPassword(encoder.encode(user.getPassword()));
            user = userRepository.save(user);
            return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting User with id{} from DB",id);
        Optional<UserApp> user = userRepository.findById(id);
        if (user.isPresent()){
            userRepository.deleteById(id);
        }else throw new GeneralException("User not found with id " + id);
    }

    // Can view all travellers who have booked on a specific flight.
    @Override
    public List<UserDTO> getAllTravellersByFlight(Long flightId) {
        Optional<Flight> existingFlights = flightRepository.findById(flightId);
        checkIfFlightExist(existingFlights);
        Flight flight = existingFlights.get();

        List<UserApp> travellers = bookingRepository.findByBookingFlightsFlight(flight)
                .stream().map(Booking::getUser).toList();

        return travellers.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public UserDTO register(UserDTO userDTO) {

        log.info("Checking if Username or Email exist");
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new GeneralException("Username already exists");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new GeneralException("Email already exists");
        }

        log.info("Creating new user");
        // Create a new UserApp entity
        UserApp user = new UserApp();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setRole(UserRoleEnum.TRAVELLER);

        UserApp savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    private void checkIfUserExist(Optional<UserApp> user) {
        if (user.isEmpty() || !user.get().isEnabled()) {
            if (user.isEmpty())
            log.error("User not found");
            else
                log.error("User is not enabled");
            throw new GeneralException("User not found");
        }
    }

    private void checkIfFlightExist(Optional<Flight> flight) {
        log.info("Checking if flight exists");
        if (flight.isEmpty()) {
            log.error("Flight not found");
            throw new GeneralException("Flight not found");
        }
    }
}
