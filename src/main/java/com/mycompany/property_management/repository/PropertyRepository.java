package com.mycompany.property_management.repository;

import com.mycompany.property_management.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Modifying
    @Query("DELETE FROM Property p WHERE p.owner.id= :ownerId")
    void deleteAllByOwnerId(Long ownerId);

    List<Property> findAllByOwnerId(Long ownerId);

}
