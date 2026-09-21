package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserLoginRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank @Length(min = 6)
    private String password;
}
