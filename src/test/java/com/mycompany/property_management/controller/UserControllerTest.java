package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.request.UserLoginRequest;
import com.mycompany.property_management.dto.request.UserPatchRequest;
import com.mycompany.property_management.dto.request.UserPutRequest;
import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.dto.response.UserResponse;
import com.mycompany.property_management.exception.DuplicateEmailException;
import com.mycompany.property_management.exception.InvalidCredentialsException;
import com.mycompany.property_management.exception.ResourceNotFoundException;
import com.mycompany.property_management.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    UserService userService;

    @Nested
    class CreateNewUserTests {

        @Test
        void shouldCreateUserSuccessfully() throws Exception {
            UserRequest userRequest = new UserRequest("TestName", "TestLastName", "test@example.com", "testPassword1234", "1234567890");
            UserResponse userResponse = new UserResponse(1L, "TestName", "TestLastName", "test@example.com", "1234567890");

            when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", "http://localhost/api/users/1"))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.email").value("test@example.com"));
        }

        @Test
        void shouldReturnBadRequestForInvalidRequestCreateBody() throws Exception {
            UserRequest userRequest = new UserRequest("", "TestLastName", "test@example.com", null, "1234567890");

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }
    }

    @Nested
    class UpdateUserTests {
        @Test
        void shouldUpdateUserSuccessfully() throws Exception {
            UserPutRequest userPutRequest = new UserPutRequest("UpdatedName", "UpdatedLastName", "0987654321");
            UserResponse userResponse = new UserResponse(1L, "UpdatedName", "UpdatedLastName", "test@example.com", "0987654321");

            when(userService.updateUser(eq(1L), any(UserPutRequest.class))).thenReturn(userResponse);

            mockMvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userPutRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.firstName").value("UpdatedName"))
                    .andExpect(jsonPath("$.lastName").value("UpdatedLastName"))
                    .andExpect(jsonPath("$.phoneNumber").value("0987654321"));
        }

        @Test
        void shouldReturnBadRequestForInvalidRequestUpdateBody() throws Exception {
            UserPutRequest userPutRequest = new UserPutRequest("", null, "1234567890");

            mockMvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userPutRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        void shouldThrowUserNotFoundException() throws Exception {
            UserPutRequest userPutRequest = new UserPutRequest("TestNamel", "TestLastName", "1234567890");

            when(userService.updateUser(eq(1L), any(UserPutRequest.class))).thenThrow(new ResourceNotFoundException("User Not Found"));

            mockMvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userPutRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User Not Found"));
        }

        @Test
        void shouldThrowDuplicateEmailExceptionException() throws Exception {
            UserPutRequest userPutRequest = new UserPutRequest("TestNamel", "TestLastName", "1234567890");

            when(userService.updateUser(eq(1L), any(UserPutRequest.class))).thenThrow(new DuplicateEmailException("Email already exists"));

            mockMvc.perform(put("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userPutRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value("Email already exists"));
        }
    }

    @Nested
    class PatchUserTests {

        @Test
        void shouldPatchUserSuccessfully() throws Exception {
            UserPatchRequest userPatchRequest = new UserPatchRequest(null, null, "0987654321");
            UserResponse userResponse = new UserResponse(1L, "TestName", "TestLastName", "test@example.com", "0987654321");

            when(userService.patchUser(eq(1L), any(UserPatchRequest.class))).thenReturn(userResponse);

            mockMvc.perform(patch("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userPatchRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.phoneNumber").value("0987654321"));
        }

        @Test
        void shouldReturnBadRequestForInvalidRequestPatchBody() throws Exception {
            UserPatchRequest userPatchRequest = new UserPatchRequest(" ", " ", "0987654321");

            mockMvc.perform(patch("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userPatchRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        void shouldThrowUserNotFoundException() throws Exception {
            UserPatchRequest userPatchRequest = new UserPatchRequest(null, null, "0987654321");

            when(userService.patchUser(eq(1L), any(UserPatchRequest.class))).thenThrow(new ResourceNotFoundException("User Not Found"));

            mockMvc.perform(patch("/api/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userPatchRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User Not Found"));
        }
    }

    @Nested
    class DeleteUserTests {
        @Test
        void shouldDeleteUserSuccessfully() throws Exception {

            mockMvc.perform(delete("/api/users/1"))
                    .andExpect(status().isNoContent());

            verify(userService).deleteUserById(1L);
        }

        @Test
        void shouldThrowUserNotFoundException() throws Exception {
            doThrow(new ResourceNotFoundException("User Not Found")).when(userService).deleteUserById(1L);

            mockMvc.perform(delete("/api/users/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User Not Found"));
        }
    }

    @Nested
    class UserLoginTests {
        @Test
        void shouldLoginSuccessfully() throws Exception {
            UserLoginRequest userLoginRequest = new UserLoginRequest("test@example.com", "password1234");

            mockMvc.perform(post("/api/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userLoginRequest)))
                    .andExpect(status().isNoContent());

            verify(userService).userLogin(any(UserLoginRequest.class));
        }

        @Test
        void shouldReturnBadRequestForInvalidLoginRequestBody() throws Exception {
            UserLoginRequest userLoginRequest = new UserLoginRequest("test", " ");

            mockMvc.perform(post("/api/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userLoginRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        void shouldThrowInvalidCredentialsException() throws Exception {
            UserLoginRequest userLoginRequest = new UserLoginRequest("test@example.com", "password1234");

            doThrow(new InvalidCredentialsException("Wrong email and/or password"))
                    .when(userService).userLogin(any(UserLoginRequest.class));

            mockMvc.perform(post("/api/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userLoginRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("Wrong email and/or password"));
        }
    }
}
