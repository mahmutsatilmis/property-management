package com.mycompany.property_management.mapper;
import com.mycompany.property_management.dto.request.PropertyPatchRequest;
import com.mycompany.property_management.dto.request.PropertyPutRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.entity.Property;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.mapper.PropertyMapper;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyMapperTest {

    private final PropertyMapper propertyMapper = new PropertyMapper();

    @Test
    void shouldMapPropertyRequestToPropertyEntity() {
        PropertyRequest propertyRequest = new PropertyRequest(
                "Test Property",
                "Test Address",
                "Test Description",
                new BigDecimal("100000.00"),
                1L
        );
        User owner = new User(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "password123",
                "1234567890"
        );

        Property property = propertyMapper.toEntity(propertyRequest, owner);

        assertThat(property.getId()).isNull();
        assertThat(property.getTitle()).isEqualTo(propertyRequest.getTitle());
        assertThat(property.getAddress()).isEqualTo(propertyRequest.getAddress());
        assertThat(property.getDescription()).isEqualTo(propertyRequest.getDescription());
        assertThat(property.getPrice()).isEqualTo(propertyRequest.getPrice());
        assertThat(property.getOwner()).isEqualTo(owner);
    }

    @Test
    void shouldUpdatePropertyEntity() {
        User owner = new User(1L, "John", "Doe", "john.doe@example.com", "password123", "1234567890");
        PropertyPutRequest propertyRequest = new PropertyPutRequest("Updated Property", "Updated Address", "Updated Description", new BigDecimal("200000.00"));
        Property property = new Property(1L,"Test Property", "Test Address", "Test Description", new BigDecimal("100000.00"), owner);

        propertyMapper.updateEntity(property, propertyRequest);

        assertThat(property.getId()).isEqualTo(1L);
        assertThat(property.getTitle()).isEqualTo(propertyRequest.getTitle());
        assertThat(property.getAddress()).isEqualTo(propertyRequest.getAddress());
        assertThat(property.getDescription()).isEqualTo(propertyRequest.getDescription());
        assertThat(property.getPrice()).isEqualTo(propertyRequest.getPrice());
        assertThat(property.getOwner()).isEqualTo(owner);
    }

    @Test
    void shouldPatchPropertyEntity() {
        User owner = new User(1L, "John", "Doe", "john.doe@example.com", "password123", "1234567890");
        PropertyPatchRequest propertyRequest = new PropertyPatchRequest(null, null, null, new BigDecimal("200000.00"));
        Property property = new Property(1L,"Test Property", "Test Address", "Test Description", new BigDecimal("100000.00"), owner);

        propertyMapper.patchEntity(property, propertyRequest);

        assertThat(property.getId()).isEqualTo(1L);
        assertThat(property.getTitle()).isEqualTo("Test Property");
        assertThat(property.getAddress()).isEqualTo("Test Address");
        assertThat(property.getDescription()).isEqualTo("Test Description");
        assertThat(property.getPrice()).isEqualTo(propertyRequest.getPrice());
        assertThat(property.getOwner()).isEqualTo(owner);
    }

    @Test
    void shouldMapPropertyEntityToPropertyResponse() {
        User owner = new User(1L, "John", "Doe", "john.doe@example.com", "password123", "1234567890");
        Property property = new Property(1L,"Test Property", "Test Address", "Test Description", new BigDecimal("100000.00"), owner);

        PropertyResponse propertyResponse = propertyMapper.toResponse(property);

        assertThat(propertyResponse.getId()).isEqualTo(property.getId());
        assertThat(propertyResponse.getTitle()).isEqualTo(property.getTitle());
        assertThat(propertyResponse.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyResponse.getDescription()).isEqualTo(property.getDescription());
        assertThat(propertyResponse.getPrice()).isEqualTo(property.getPrice());
        assertThat(propertyResponse.getOwner().getId()).isEqualTo(owner.getId());
        assertThat(propertyResponse.getOwner().getFirstName()).isEqualTo(owner.getFirstName());
        assertThat(propertyResponse.getOwner().getLastName()).isEqualTo(owner.getLastName());
    }
}