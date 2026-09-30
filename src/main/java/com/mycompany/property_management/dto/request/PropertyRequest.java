package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


import java.math.BigDecimal;

@Getter
public class PropertyRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String address;
    @NotBlank
    private String description;
    @NotNull @DecimalMin("10000.00")
    private BigDecimal price;
    @NotNull
    private Long ownerId;

}
