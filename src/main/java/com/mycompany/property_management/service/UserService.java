package com.mycompany.property_management.service;

import com.mycompany.property_management.dto.request.PatchUserRequest;
import com.mycompany.property_management.dto.request.UserLoginRequest;
import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.dto.response.UserResponse;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.exception.DuplicateEmailException;
import com.mycompany.property_management.exception.InvalidCredentialsException;
import com.mycompany.property_management.exception.ResourceNotFoundException;
import com.mycompany.property_management.mapper.UserMapper;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PropertyRepository propertyRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                       PropertyRepository propertyRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.propertyRepository = propertyRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new DuplicateEmailException("Email already exists");
        }
        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        if(userRepository.existsByEmail(userRequest.getEmail()) && !user.getEmail().equalsIgnoreCase(userRequest.getEmail())){
            throw new DuplicateEmailException("Email already exists");
        }
        userMapper.toPutEntity(userRequest, user);
        user =  userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponse patchUser(Long id, PatchUserRequest patchUserRequest) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );
        if(patchUserRequest.getEmail() != null &&
                userRepository.existsByEmail(patchUserRequest.getEmail())
                && !user.getEmail().equalsIgnoreCase(patchUserRequest.getEmail())){
            throw new DuplicateEmailException("Email belongs to another user");
        }
        userMapper.toPatchEntity(patchUserRequest, user);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        propertyRepository.deleteAllByOwnerId(userId);
        userRepository.deleteById(userId);
    }

    public void userLogin(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow(
                () -> new InvalidCredentialsException("Wrong email and/or password")
        );
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Wrong email and/or password");
        }
    }




}
