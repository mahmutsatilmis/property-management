package com.mycompany.property_management.service;

import com.mycompany.property_management.dto.request.PropertyPatchRequest;
import com.mycompany.property_management.dto.request.PropertyPutRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.entity.Property;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.exception.ResourceNotFoundException;
import com.mycompany.property_management.mapper.PropertyMapper;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PropertyService {
    private final PropertyMapper propertyMapper;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyService(PropertyMapper propertyMapper, PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyMapper = propertyMapper;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }


    public PropertyResponse createProperty(PropertyRequest propertyRequest) {
        User owner = userRepository.findById(propertyRequest.getOwnerId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        Property property = propertyMapper.toEntity(propertyRequest, owner);
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property);
    }

    public List<PropertyResponse> getAllProperties() {
        // TODO: Understand and fix potential N+1 query problem
        return propertyRepository.findAll()
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    public void deleteById(Long id) {
        propertyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Property not found")
        );
        propertyRepository.deleteById(id);
    }


    public PropertyResponse updateProperty(Long id, PropertyPutRequest propertyRequest) {
        Property property = propertyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Property not found")
        );
        propertyMapper.toPutEntity(property, propertyRequest);
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property);
    }


    public PropertyResponse patchProperty(Long id, PropertyPatchRequest propertyRequest) {
        Property property = propertyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Property not found")
        );
        propertyMapper.toPatchEntity(property, propertyRequest);
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property);
    }

    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Property not found")
        );
        return propertyMapper.toResponse(property);
    }

    public List<PropertyResponse> getPropertyByOwnerId(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return propertyRepository.findAllByOwnerId(ownerId).stream().
                map(propertyMapper::toResponse).toList();
    }
}
