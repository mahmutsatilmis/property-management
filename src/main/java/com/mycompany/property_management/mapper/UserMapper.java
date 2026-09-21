package com.mycompany.property_management.mapper;

import com.mycompany.property_management.dto.request.PatchUserRequest;
import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.dto.response.UserResponse;
import com.mycompany.property_management.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        return user;
    }

    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        return userResponse;
    }

    public void toPutEntity(UserRequest userRequest, User user){
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
    }

    public void toPatchEntity(PatchUserRequest patchUserRequest, User user) {
        if (patchUserRequest.getEmail() != null) {
            user.setEmail(patchUserRequest.getEmail());
        }
        if (patchUserRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(patchUserRequest.getPhoneNumber());
        }
        if (patchUserRequest.getFirstName() != null) {
            user.setFirstName(patchUserRequest.getFirstName());
        }
        if (patchUserRequest.getLastName() != null) {
            user.setLastName(patchUserRequest.getLastName());
        }
    }
}
