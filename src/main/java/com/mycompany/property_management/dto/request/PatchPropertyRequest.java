package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchPropertyRequest {

    private String title;
    private String address;
    private String description;
    private String ownerName;
    @Email
    private String ownerEmail;
    private Long price;
}
