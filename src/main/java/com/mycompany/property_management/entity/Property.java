package com.mycompany.property_management.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PROPERTY")
@Getter 
@Setter 
public class Property {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(name="PROPERTY_TITLE", nullable = false)
    private String title;
    @Column(name="PROPERTY_ADDRESS")
    private String address;
    @Column(name="PROPERTY_DESCRIPTION", nullable = false)
    private String description;
    @Column(name="OWNER_NAME", nullable=false)
    private String ownerName;
    @Column(name="OWNER_EMAIL", nullable = false)
    private String ownerEmail;
    @Column(name="PROPERTY_PRICE", nullable = false)
    private Long price;

}
