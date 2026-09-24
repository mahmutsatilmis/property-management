package com.mycompany.property_management.dto.request;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class PatchPropertyRequest {

    private String title;
    private String address;
    private String description;
    private BigDecimal price;
}
