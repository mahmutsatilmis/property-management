package com.mycompany.property_management.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class PropertyResponse {
    private Long id;
    private String title;
    private String address;
    private String description;
    private String ownerName;
    private String ownerEmail;
    private double price;

}
