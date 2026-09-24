package com.mycompany.property_management.dto.request;

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
    @NotNull
    private BigDecimal price;

}
