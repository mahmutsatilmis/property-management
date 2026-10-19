package com.mycompany.property_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private Long id;
    private String title;
    private String address;
    private String description;
    private BigDecimal price;
    private OwnerResponse owner;
}
