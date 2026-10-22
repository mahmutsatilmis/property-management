package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPutRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String phoneNumber;

}
