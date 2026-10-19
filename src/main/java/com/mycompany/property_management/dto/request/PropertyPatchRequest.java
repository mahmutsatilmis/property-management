package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyPatchRequest {

    private String title;
    private String address;
    private String description;
    @DecimalMin("10000.00")
    private BigDecimal price;
}
