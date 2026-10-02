package com.mycompany.property_management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class PropertyPatchRequest {

    private String title;
    private String address;
    private String description;
    @DecimalMin("10000.00")
    private BigDecimal price;
}
