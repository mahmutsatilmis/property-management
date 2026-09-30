package com.mycompany.property_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PROPERTY")
@Getter 
@Setter
public class Property {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="property_title", nullable = false)
    private String title;
    @Column(name="property_address")
    private String address;
    @Column(name="property_description", nullable = false)
    private String description;
    @Column(name="property_price", nullable = false)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

}
