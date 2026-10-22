package com.mycompany.property_management.service;

import com.mycompany.property_management.dto.request.PropertyPatchRequest;
import com.mycompany.property_management.dto.request.PropertyPutRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.OwnerResponse;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.entity.Property;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.exception.ResourceNotFoundException;
import com.mycompany.property_management.mapper.PropertyMapper;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyMapper propertyMapper;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PropertyService propertyService;

    private Property property;


    @Nested
    class CreatePropertyTests {

        private User owner;
        private PropertyRequest propertyRequest;
        private PropertyResponse propertyResponse;

        @BeforeEach
        void setUp() {
            owner = new User(1L, "Ali", "Khan", "ali.khan@example.com", "password123", "1234567890");
            OwnerResponse ownerResponse = new OwnerResponse(1L, "Ali", "Khan");
            property = new Property(1L, "Beautiful House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), owner);
            propertyResponse = new PropertyResponse(1L, "Beautiful House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), ownerResponse);
            propertyRequest = new PropertyRequest("Beautiful House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), 1L);
        }

        @Test
        void shouldCreatePropertySuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
            when(propertyMapper.toEntity(propertyRequest, owner)).thenReturn(property);
            when(propertyRepository.save(property)).thenReturn(property);
            when(propertyMapper.toResponse(property)).thenReturn(propertyResponse);

            PropertyResponse result = propertyService.createProperty(propertyRequest);

            assertThat(result).isEqualTo(propertyResponse);
            verify(propertyRepository).save(property);
        }

        @Test
        void shouldThrowExceptionWhenOwnerNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> propertyService.createProperty(propertyRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");
            verify(propertyRepository, never()).save(any(Property.class));
        }
    }

    @Nested
    class DeletePropertyTests {

        @Test
        void shouldDeletePropertyById() {
            Long propertyId = 1L;
            property = new Property();
            property.setId(propertyId);

            when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));

            propertyService.deleteById(propertyId);

            verify(propertyRepository).deleteById(propertyId);
        }

        @Test
        void shouldThrowExceptionWhenPropertyNotFound() {
            Long propertyId = 1L;
            when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> propertyService.deleteById(propertyId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Property not found");
            verify(propertyRepository, never()).deleteById(propertyId);
        }
    }

    @Nested
    class UpdatePropertyTests {

        private PropertyPutRequest propertyPutRequest;
        private PropertyResponse propertyResponse;

        @BeforeEach
        void setUp() {
            property = new Property(1L, "Beautiful House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), new User());
            propertyPutRequest = new PropertyPutRequest("Updated House", "456 Elm St", "An updated description", new BigDecimal(12000000));
            propertyResponse = new PropertyResponse(1L, "Updated House", "456 Elm St", "An updated description", new BigDecimal(12000000), new OwnerResponse());
        }

        @Test
        void shouldUpdatePropertySuccessfully() {
            when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
            when(propertyRepository.save(property)).thenReturn(property);
            when(propertyMapper.toResponse(property)).thenReturn(propertyResponse);

            PropertyResponse result = propertyService.updateProperty(1L, propertyPutRequest);

            assertThat(result).isEqualTo(propertyResponse);
            verify(propertyMapper).updateEntity(property, propertyPutRequest);
            verify(propertyRepository).save(property);
        }

        @Test
        void shouldThrowExceptionWhenPropertyNotFound() {
            when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> propertyService.updateProperty(1L, propertyPutRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Property not found");
            verify(propertyRepository, never()).save(any(Property.class));
        }
    }

    @Nested
    class PatchPropertyTests {

        private PropertyPatchRequest propertyPatchRequest;
        private PropertyResponse propertyResponse;

        @BeforeEach
        void setUp() {
            User owner = new User(1L, "Ali", "Khan", "ali.khan@example.com", "password123", "1234567890");
            OwnerResponse ownerResponse = new OwnerResponse(1L, "Ali", "Khan");
            property = new Property(1L, "Beautiful House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), owner);
            propertyPatchRequest = new PropertyPatchRequest("Updated House", null, null, null);
            propertyResponse = new PropertyResponse(1L, "Updated House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), ownerResponse);
        }

        @Test
        void shouldPatchPropertySuccessfully() {
            when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
            when(propertyRepository.save(property)).thenReturn(property);
            when(propertyMapper.toResponse(property)).thenReturn(propertyResponse);

            PropertyResponse result = propertyService.patchProperty(1L, propertyPatchRequest);

            assertThat(result).isEqualTo(propertyResponse);

            verify(propertyMapper).patchEntity(property, propertyPatchRequest);
            verify(propertyRepository).save(property);
        }

        @Test
        void shouldThrowExceptionWhenPropertyNotFound() {
            when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> propertyService.patchProperty(1L, propertyPatchRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Property not found");

            verify(propertyRepository, never()).save(any(Property.class));
        }


    }

    @Nested
    class GetPropertyByIdTests {

        private PropertyResponse propertyResponse;

        @BeforeEach
        void setUp() {
            User owner = new User(1L, "Ali", "Khan", "ali.khan@example.com", "password123", "1234567890");
            OwnerResponse ownerResponse = new OwnerResponse(1L, "Ali", "Khan");
            property = new Property(1L, "Beautiful House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), owner);
            propertyResponse = new PropertyResponse(1L, "Beautiful House", "123 Main St", "A beautiful house with a garden", new BigDecimal(10000000), ownerResponse);
        }

        @Test
        void shouldGetPropertyById() {
            when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
            when(propertyMapper.toResponse(property)).thenReturn(propertyResponse);

            PropertyResponse result = propertyService.getPropertyById(1L);

            assertThat(result).isNotNull().isEqualTo(propertyResponse);
        }

        @Test
        void shouldThrowExceptionWhenPropertyNotFound() {
            when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> propertyService.getPropertyById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Property not found");
        }
    }

    @Nested
    class GetAllPropertiesByOwnerIdTests {

        private PropertyResponse propertyResponse;
        private PropertyResponse propertyResponse2;
        private Property property1;
        private Property property2;
        private List<Property> propertyList;
        private List<PropertyResponse> propertyResponseList;

        @BeforeEach
        void setUp() {
            User owner = new User(2L, "John", "Doe", "john.doe@example.com", "password456", "0987654321");
            OwnerResponse ownerResponse = new OwnerResponse(2L, "John", "Doe");
            property1 = new Property(2L, "Modern Apartment", "456 Elm St", "A modern apartment in the city center", new BigDecimal(8000000), owner);
            property2 = new Property(3L, "Cozy Cottage", "789 Oak St", "A cozy cottage in the countryside", new BigDecimal(6000000), owner);
            propertyResponse = new PropertyResponse(2L, "Modern Apartment", "456 Elm St", "A modern apartment in the city center", new BigDecimal(8000000), ownerResponse);
            propertyResponse2 = new PropertyResponse(3L, "Cozy Cottage", "789 Oak St", "A cozy cottage in the countryside", new BigDecimal(6000000), ownerResponse);
            propertyResponseList = List.of(propertyResponse, propertyResponse2);
            propertyList = List.of(property1, property2);

        }

        @Test
        void shouldGetAllPropertiesByOwnerId() {
            when(userRepository.existsById(2L)).thenReturn(true);
            when(propertyRepository.findAllByOwnerId(2L)).thenReturn(propertyList);
            when(propertyMapper.toResponse(property1)).thenReturn(propertyResponse);
            when(propertyMapper.toResponse(property2)).thenReturn(propertyResponse2);

            List<PropertyResponse> result = propertyService.getPropertyByOwnerId(2L);

            assertThat(result).isEqualTo(propertyResponseList);
        }

        @Test
        void shouldThrowExceptionWhenOwnerNotFound() {
            when(userRepository.existsById(2L)).thenReturn(false);

            assertThatThrownBy(() -> propertyService.getPropertyByOwnerId(2L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");

            verify(propertyRepository, never()).findAllByOwnerId(2L);
        }

    }
}