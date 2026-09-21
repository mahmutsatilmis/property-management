package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class PatchPropertyRequest {

    private String title;
    private String address;
    private String description;
    private BigDecimal price;
}
