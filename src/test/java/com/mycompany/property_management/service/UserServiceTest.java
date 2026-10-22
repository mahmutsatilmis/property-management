package com.mycompany.property_management.service;

import com.mycompany.property_management.dto.request.UserLoginRequest;
import com.mycompany.property_management.dto.request.UserPatchRequest;
import com.mycompany.property_management.dto.request.UserPutRequest;
import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.dto.response.UserResponse;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.exception.DuplicateEmailException;
import com.mycompany.property_management.exception.InvalidCredentialsException;
import com.mycompany.property_management.exception.ResourceNotFoundException;
import com.mycompany.property_management.mapper.UserMapper;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User user = new User(1L, "Ali", "Khan", "ali.khan@example.com", "password123", "1234567890");

    @Nested
    class CreateUserTests {

        private UserRequest userRequest;
        private UserResponse userResponse;

        @BeforeEach
        void setUp() {
            userRequest = new UserRequest("Ali", "Khan", "ali.khan@example.com", "password123", "1234567890");
            userResponse = new UserResponse(1L, "Ali", "Khan", "ali.khan@example.com", "1234567890");
        }

        @Test
        void shouldCreateUserWithValidData() {
            when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
            when(userMapper.toEntity(userRequest)).thenReturn(user);
            when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toResponse(user)).thenReturn(userResponse); // Mock the response mapping

            UserResponse result = userService.createUser(userRequest);

            assertThat(result).isEqualTo(userResponse);
            verify(passwordEncoder).encode(userRequest.getPassword());
            verify(userMapper).toEntity(userRequest);
            verify(userRepository).save(argThat(savedUser -> savedUser.getPassword().equals("encodedPassword")));
        }

        @Test
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);
            assertThatThrownBy(() -> userService.createUser(userRequest))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessage("Email already exists");
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    class UpdateUserTests {

        private UserPutRequest userRequest;
        private UserResponse userResponse;

        @BeforeEach
        void setUp() {
            userRequest = new UserPutRequest("Veli", "Khan", "1234567809");
            userResponse = new UserResponse(1L, "Veli", "Khan", "ali.khan@example.com", "1234567809");
        }

        @Test
        void shouldUpdateUserWithValidData() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toResponse(user)).thenReturn(userResponse);

            UserResponse result = userService.updateUser(1L, userRequest);

            assertThat(result).isEqualTo(userResponse);
            verify(userMapper).updateEntity(userRequest, user);
            verify(userRepository).save(user);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.updateUser(1L, userRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    class PatchUserTests {

        private UserResponse userResponse;
        private UserPatchRequest userPatchRequest;

        @BeforeEach
        void setUp() {
            userResponse = new UserResponse(1L, "Ali", "Khan", "ali.khan@example.com", "1234567809");
            userPatchRequest = new UserPatchRequest(null, null, "1234567809");
        }

        @Test
        void shouldPatchUserWithValidData() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toResponse(user)).thenReturn(userResponse);

            UserResponse result = userService.patchUser(1L, userPatchRequest);

            assertThat(result).isEqualTo(userResponse);
            verify(userMapper).patchEntity(userPatchRequest, user);
            verify(userRepository).save(user);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.patchUser(1L, userPatchRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    class DeleteUserByIdTests {

        @Test
        void shouldDeleteUserWhenExists() {
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            userService.deleteUserById(userId);

            InOrder inOrder = inOrder(propertyRepository, userRepository);
            inOrder.verify(propertyRepository).deleteAllByOwnerId(userId);
            inOrder.verify(userRepository).deleteById(userId);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.deleteUserById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");

            verify(propertyRepository, never()).deleteAllByOwnerId(anyLong());
            verify(userRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    class UserLoginTests {

        private UserLoginRequest userLoginRequest;

        @BeforeEach
        void setUp() {
            userLoginRequest = new UserLoginRequest("ali.khan@example.com", "password123");
        }

        @Test
        void shouldLoginUserWithValidCredentials() {
            when(userRepository.findByEmail("ali.khan@example.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())).thenReturn(true);

            userService.userLogin(userLoginRequest);

            verify(passwordEncoder).matches(userLoginRequest.getPassword(), user.getPassword());
        }

        @Test
        void shouldThrowExceptionWithWrongEmail() {
            when(userRepository.findByEmail("ali.khan@example.com")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.userLogin(userLoginRequest))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Wrong email and/or password");

            verify(passwordEncoder, never()).matches(anyString(), anyString());
        }

        @Test
        void shouldThrowExceptionWithWrongPassword() {
            when(userRepository.findByEmail("ali.khan@example.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())).thenReturn(false);
            assertThatThrownBy(() -> userService.userLogin(userLoginRequest))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Wrong email and/or password");

            verify(passwordEncoder).matches(userLoginRequest.getPassword(), user.getPassword());
        }
    }
}
