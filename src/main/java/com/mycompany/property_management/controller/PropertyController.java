package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.request.PatchPropertyRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.service.PropertyService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@Tag(name = "Properties")
@RestController
@RequestMapping ("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @ApiResponse(responseCode = "400", description = "Request Body failed validation")
    @ApiResponse(responseCode = "404", description = "User Not Found")
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest propertyRequest, @RequestParam Long ownerId) {
        PropertyResponse response = propertyService.createProperty(propertyRequest, ownerId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{propertyId}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @ApiResponse(responseCode = "400", description = "Request Body failed validation")
    @ApiResponse(responseCode = "404", description = "Property Not Found")
    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse> updateProperty(@PathVariable Long propertyId, @Valid @RequestBody PropertyRequest propertyRequest){
        PropertyResponse response = propertyService.updateProperty(propertyId, propertyRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> deletePropertyById(@PathVariable Long propertyId) {
        propertyService.deleteById(propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {

        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @ApiResponse(responseCode = "400", description = "Request Body failed validation")
    @ApiResponse(responseCode = "404", description = "Property Not Found")
    @PatchMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse> patchProperty(@PathVariable Long propertyId, @Valid @RequestBody PatchPropertyRequest propertyRequest) {
        PropertyResponse propertyResponse = propertyService.patchProperty(propertyId, propertyRequest);
        return ResponseEntity.ok(propertyResponse);
    }
    //TODO Add GET /{propertyId} endpoing

}
