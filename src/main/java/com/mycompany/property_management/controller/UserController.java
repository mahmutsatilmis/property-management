package com.mycompany.property_management.controller;


import com.mycompany.property_management.dto.request.PatchUserRequest;
import com.mycompany.property_management.dto.request.UserLoginRequest;
import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.dto.response.UserResponse;
import com.mycompany.property_management.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponse(responseCode = "400", description = "Request Body failed validation")
    @PostMapping
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(userResponse.getId())
                .toUri();
        return ResponseEntity.created(location).body(userResponse);
    }

    @ApiResponse(responseCode = "400", description = "Request Body failed validation")
    @ApiResponse(responseCode = "404", description = "User Not Found")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest userRequest, @PathVariable Long userId) {
        UserResponse userResponse = userService.updateUser(userId, userRequest);
        return  ResponseEntity.ok(userResponse);
    }

    @ApiResponse(responseCode = "400", description = "Request Body failed validation")
    @ApiResponse(responseCode = "404", description = "User Not Found")
    @ApiResponse(responseCode = "409", description = "Email already exists")
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

    @ApiResponse(responseCode = "400", description = "Request Body failed validation")
    @ApiResponse(responseCode = "401", description = "Wrong email and/or password")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(userService.userLogin(userLoginRequest));
    }


}
