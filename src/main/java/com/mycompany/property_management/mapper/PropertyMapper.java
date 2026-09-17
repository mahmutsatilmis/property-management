package com.mycompany.property_management.mapper;

import com.mycompany.property_management.dto.request.PatchPropertyRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.entity.Property;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper {

    public Property toEntity(PropertyRequest propertyRequest){
        Property property=new Property();
        property.setTitle(propertyRequest.getTitle());
        property.setDescription(propertyRequest.getDescription());
        property.setPrice(propertyRequest.getPrice());
        property.setAddress(propertyRequest.getAddress());
        property.setOwnerEmail(propertyRequest.getOwnerEmail());
        property.setOwnerName(propertyRequest.getOwnerName());
        return property;
    }

    public Property updateEntityFromPut(Property property, PropertyRequest propertyRequest){
        property.setTitle(propertyRequest.getTitle());
        property.setDescription(propertyRequest.getDescription());
        property.setPrice(propertyRequest.getPrice());
        property.setAddress(propertyRequest.getAddress());
        property.setOwnerEmail(propertyRequest.getOwnerEmail());
        property.setOwnerName(propertyRequest.getOwnerName());
        return property;
    }

    public Property updateEntityFromPatch(Property property, PatchPropertyRequest propertyRequest){
        if (propertyRequest.getTitle() != null) {
            property.setTitle(propertyRequest.getTitle());
        }
        if (propertyRequest.getDescription() != null) {
            property.setDescription(propertyRequest.getDescription());
        }
        if (propertyRequest.getPrice() != null) {
            property.setPrice(propertyRequest.getPrice());
        }
        if (propertyRequest.getAddress() != null) {
            property.setAddress(propertyRequest.getAddress());
        }
        if (propertyRequest.getOwnerEmail() != null) {
            property.setOwnerEmail(propertyRequest.getOwnerEmail());
        }
        if (propertyRequest.getOwnerName() != null) {
            property.setOwnerName(propertyRequest.getOwnerName());
        }
        return property;
    }


    public PropertyResponse toResponse(Property property){
        PropertyResponse propertyResponse=new PropertyResponse();
        propertyResponse.setId(property.getId());
        propertyResponse.setTitle(property.getTitle());
        propertyResponse.setDescription(property.getDescription());
        propertyResponse.setPrice(property.getPrice());
        propertyResponse.setAddress(property.getAddress());
        propertyResponse.setOwnerEmail(property.getOwnerEmail());
        propertyResponse.setOwnerName(property.getOwnerName());
        return propertyResponse;
    }
}
