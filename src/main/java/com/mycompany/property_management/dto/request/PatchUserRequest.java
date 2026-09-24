package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class PatchUserRequest {
    @Pattern(regexp = "\\s*+\\S.*", message = "First name cannot be blank")
    private String firstName;
    @Pattern(regexp = "\\s*+\\S.*", message = "Last name cannot be blank")
    private String lastName;
    @Email
    @Pattern(regexp = "\\s*+\\S.*", message = "Email cannot be blank")
    private String email;
    private String phoneNumber;
}

