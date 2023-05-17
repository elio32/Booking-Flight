package com.project.BookingFlight.service.impl;

import com.project.BookingFlight.exception.GeneralException;
import com.project.BookingFlight.mapper.UserMapper;
import com.project.BookingFlight.model.dto.UserDTO;
import com.project.BookingFlight.model.entity.UserApp;
import com.project.BookingFlight.repository.UserRepository;
import com.project.BookingFlight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserApp> user = userRepository.findByUsername(username);
        log.info("Checking if this username {} exist",username);
        if (user.isEmpty() || !user.get().isEnabled()){
            log.info("Username {} not found",username);
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        return new User(user.get().getUsername(),user.get().getPassword(),user.get().getAuthorities());
    }

    @Override
    public List<UserDTO> getAllUsers() { //pagination here
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.info("Fetching User{} from DB",email);
        Optional<UserApp> user = userRepository.findByEmail(email);
        checkIfExist(user);
        return userMapper.toDto(user.get());//jep error pa .get

    }

    @Override
    public UserDTO getUserById(Long id) {
        log.info("fetching User with id{} from DB",id);
        Optional<UserApp> user = userRepository.findById(id);
        checkIfExist(user);
        return userMapper.toDto(user.get());
    }

    @Override
    public UserDTO updateUser(Long id, UserApp requestedUser) {
        Optional<UserApp> existingUser = userRepository.findById(id);
        checkIfExist(existingUser);
        UserApp user = existingUser.get();
        log.info("Updating user{}",user.getUsername());
        user.setPassword(existingUser.get().getPassword());
        if(requestedUser.getUsername() != null) {
            if(userRepository.findByUsername(requestedUser.getUsername()).isPresent())
                throw new GeneralException("Username is taken!", Arrays.asList(requestedUser.getUsername()));
            user.setUsername(requestedUser.getUsername());
        }
        if (requestedUser.getFirstName() != null) {
            user.setFirstName(requestedUser.getFirstName());}
        if (requestedUser.getLastName() != null){
            user.setLastName(requestedUser.getLastName());}
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
    public void deleteUser(Long id) {
        log.info("Deleting User with id{} from DB",id);
        Optional<UserApp> user = userRepository.findById(id);
        if (user.isPresent()){
            userRepository.deleteById(id);
        }else throw new GeneralException("User not found with id " + id,null);


    }
    private void checkIfExist(Optional<UserApp> user) {
        if (user.isEmpty() || !user.get().isEnabled()) {
            if (user.isEmpty())
            log.error("User not found");
            else
                log.error("User is not enabled");
            throw new GeneralException("User not found", null);
        }
    }
}
