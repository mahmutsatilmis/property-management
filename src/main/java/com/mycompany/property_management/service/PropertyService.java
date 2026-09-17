package com.mycompany.property_management.service;

import com.mycompany.property_management.dto.request.PatchPropertyRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.PropertyResponse;

import java.util.List;

public interface PropertyService {

    PropertyResponse saveProperty(PropertyRequest propertyRequest);

    List<PropertyResponse> getAllProperties();

    void deleteById(Long id);

    PropertyResponse putProperty(Long id, PropertyRequest propertyRequest);

    PropertyResponse patchProperty(Long id, PatchPropertyRequest propertyRequest);
}
