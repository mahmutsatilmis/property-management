package com.mycompany.property_management.service.impl;

import com.mycompany.property_management.dto.request.PatchPropertyRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.entity.Property;
import com.mycompany.property_management.mapper.PropertyMapper;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.service.PropertyService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyMapper propertyMapper;
    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyMapper propertyMapper, PropertyRepository propertyRepository) {
        this.propertyMapper = propertyMapper;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyResponse saveProperty(PropertyRequest propertyRequest) {
        Property property = propertyMapper.toEntity(propertyRequest);
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property);
    }

    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll()
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    public void deleteById(Long id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public PropertyResponse putProperty(Long id, PropertyRequest propertyRequest) {
        Property property = propertyRepository.findById(id).orElseThrow();
        property = propertyMapper.updateEntityFromPut(property, propertyRequest);
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property);
    }

    @Override
    public PropertyResponse patchProperty(Long id, PatchPropertyRequest propertyRequest) {
        Property property = propertyRepository.findById(id).orElseThrow();
        property = propertyMapper.updateEntityFromPatch(property, propertyRequest);
        property = propertyRepository.save(property);
        return propertyMapper.toResponse(property);
    }
}
