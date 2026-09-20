package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.request.PatchPropertyRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController 
@RequestMapping ("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    
    @PostMapping
    public ResponseEntity<PropertyResponse> saveProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
        PropertyResponse response = propertyService.saveProperty(propertyRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
        //return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyRequest propertyRequest){
        PropertyResponse response = propertyService.putProperty(id, propertyRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePropertyById(@PathVariable Long id) {
        propertyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {

        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PropertyResponse> patchProperty(@PathVariable Long id, @Valid @RequestBody PatchPropertyRequest propertyRequest) {
        PropertyResponse propertyResponse = propertyService.patchProperty(id, propertyRequest);
        return ResponseEntity.ok(propertyResponse);
    }

}
