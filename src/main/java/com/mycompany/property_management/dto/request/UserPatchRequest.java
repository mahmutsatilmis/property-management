package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchRequest {
    @Pattern(regexp = "\\s*+\\S.*", message = "First name cannot be blank")
    private String firstName;
    @Pattern(regexp = "\\s*+\\S.*", message = "Last name cannot be blank")
    private String lastName;
    private String phoneNumber;
}

