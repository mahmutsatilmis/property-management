package com.mycompany.property_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter 
@Setter
public class PropertyResponse {
    private Long id;
    private String title;
    private String address;
    private String description;
    private BigDecimal price;

}
