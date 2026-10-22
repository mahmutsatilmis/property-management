package com.mycompany.property_management.mapper;

import com.mycompany.property_management.dto.request.UserPatchRequest;
import com.mycompany.property_management.dto.request.UserPutRequest;
import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.dto.response.UserResponse;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldMapUserRequestToUserEntity() {
        UserRequest userRequest = new UserRequest("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        User user = userMapper.toEntity(userRequest);

        assertThat(user.getFirstName()).isEqualTo(userRequest.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userRequest.getLastName());
        assertThat(user.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(user.getPassword()).isNull(); // Password is not set in the mapper, it is handled in the service layer
        assertThat(user.getPhoneNumber()).isEqualTo(userRequest.getPhoneNumber());
    }

    @Test
    void shouldMapUserToUserResponse() {
        User user = new User(1L,"John", "Doe", "john.doe@example.com", "password123", "1234567890");
        UserResponse userResponse = userMapper.toResponse(user);

        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }

    @Test
    void shouldUpdateUserEntity() {
        UserPutRequest userRequest = new UserPutRequest("UpdatedJohn", "UpdatedDoe", "0123456789");
        User user = new User(1L,"John", "Doe", "john.doe@example.com", "password123", "1234567890");

        userMapper.updateEntity(userRequest,user);

        assertThat(user.getFirstName()).isEqualTo(userRequest.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userRequest.getLastName());
        assertThat(user.getPhoneNumber()).isEqualTo(userRequest.getPhoneNumber());
    }

    @Test
    void shouldPatchUserEntity() {
        User user = new User(1L,"John", "Doe", "john.doe@example.com", "password123", "1234567890");
        UserPatchRequest userRequest = new UserPatchRequest(null, null, "0123456789");

        userMapper.patchEntity(userRequest, user);

        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPhoneNumber()).isEqualTo(userRequest.getPhoneNumber());
    }
}