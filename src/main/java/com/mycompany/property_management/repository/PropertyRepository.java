package com.mycompany.property_management.repository;

import com.mycompany.property_management.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {

}
