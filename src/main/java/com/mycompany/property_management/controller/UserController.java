package com.mycompany.property_management.controller;


import com.mycompany.property_management.dto.request.PatchUserRequest;
import com.mycompany.property_management.dto.request.UserLoginRequest;
import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.dto.response.UserResponse;
import com.mycompany.property_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody UserRequest userRequest, Long ownerId) {
        UserResponse userResponse = userService.createUser(userRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(userResponse.getId())
                .toUri();
        return ResponseEntity.created(location).body(userResponse);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest userRequest, @PathVariable Long userId) {
        UserResponse userResponse = userService.updateUser(userId, userRequest);
        return  ResponseEntity.ok(userResponse);
    }
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> patchUser(@RequestBody @Valid PatchUserRequest patchUserRequest, @PathVariable Long userId) {
        UserResponse userResponse = userService.patchUser(userId, patchUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(userService.userLogin(userLoginRequest));
    }


}
