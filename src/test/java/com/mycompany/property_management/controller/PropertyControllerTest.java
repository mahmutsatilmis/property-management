package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.request.PropertyPatchRequest;
import com.mycompany.property_management.dto.request.PropertyPutRequest;
import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.dto.response.OwnerResponse;
import com.mycompany.property_management.dto.response.PropertyResponse;
import com.mycompany.property_management.exception.ResourceNotFoundException;
import com.mycompany.property_management.service.PropertyService;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private PropertyService propertyService;


    @Nested
    class CreatePropertyTests {
        @Test
        void shouldCreatePropertySuccessfully() throws Exception {
            OwnerResponse owner = new OwnerResponse(1L, "John", "Doe");
            PropertyRequest propertyRequest = new PropertyRequest("Test Property", "123 Test St", "A lovely property", new BigDecimal(1000000), 1L);
            PropertyResponse response = new PropertyResponse(1L, "Test Property", "123 Test St", "A lovely property", new BigDecimal(1000000), owner);

            when(propertyService.createProperty(any(PropertyRequest.class))).thenReturn(response);

            mockMvc.perform(post("/api/properties")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location" , "http://localhost/api/properties/1"))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Test Property"))
                    .andExpect(jsonPath("$.address").value("123 Test St"))
                    .andExpect(jsonPath("$.description").value("A lovely property"))
                    .andExpect(jsonPath("$.price").value(1000000))
                    .andExpect(jsonPath("$.owner.id").value(1L))
                    .andExpect(jsonPath("$.owner.firstName").value("John"))
                    .andExpect(jsonPath("$.owner.lastName").value("Doe"));

        }


        @Test
        void shouldReturnBadRequestForInvalidInput() throws Exception {
            PropertyRequest propertyRequest = new PropertyRequest("", "", "", new BigDecimal(5000), null);

            mockMvc.perform(post("/api/properties")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(propertyService);
        }

        @Test
        void shouldReturnNotFoundForNonExistentUser() throws Exception {
            PropertyRequest propertyRequest = new PropertyRequest("Test Property", "123 Test St", "A lovely property", new BigDecimal(1000000), 1L);

            when(propertyService.createProperty(any(PropertyRequest.class))).thenThrow(new ResourceNotFoundException("User Not Found"));

            mockMvc.perform(post("/api/properties")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User Not Found"));

        }
    }

    @Nested
    class UpdatePropertyTests {
        @Test
        void shouldUpdatePropertySuccessfully() throws Exception {
            OwnerResponse owner = new OwnerResponse(1L, "John", "Doe");
            PropertyPutRequest propertyPutRequest = new PropertyPutRequest("Updated Property", "456 Updated St", "An updated lovely property", new BigDecimal(2000000));
            PropertyResponse propertyResponse = new PropertyResponse(1L, "Updated Property", "456 Updated St", "An updated lovely property", new BigDecimal(2000000), owner);

            when(propertyService.updateProperty(eq(1L), any(PropertyPutRequest.class))).thenReturn(propertyResponse);

            mockMvc.perform(put("/api/properties/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyPutRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Updated Property"))
                    .andExpect(jsonPath("$.address").value("456 Updated St"))
                    .andExpect(jsonPath("$.description").value("An updated lovely property"))
                    .andExpect(jsonPath("$.price").value(2000000))
                    .andExpect(jsonPath("$.owner.id").value(1L))
                    .andExpect(jsonPath("$.owner.firstName").value("John"))
                    .andExpect(jsonPath("$.owner.lastName").value("Doe"));
        }

        @Test
        void shouldReturnNotFoundForNonExistentProperty() throws Exception {
            PropertyPutRequest propertyPutRequest = new PropertyPutRequest("Updated Property", "456 Updated St", "An updated lovely property", new BigDecimal(2000000));

            when(propertyService.updateProperty(eq(1L), any(PropertyPutRequest.class))).thenThrow(new ResourceNotFoundException("Property Not Found"));

            mockMvc.perform(put("/api/properties/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyPutRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Property Not Found"));
        }
        @Test
        void shouldReturnBadRequestForInvalidInput() throws Exception {
            PropertyPutRequest propertyPutRequest = new PropertyPutRequest("Updated Title", "", "Updated Description", null);

            mockMvc.perform(put("/api/properties/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyPutRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(propertyService);
        }
    }

    @Nested
    class PatchPropertyTests {
        @Test
        void shouldPatchPropertySuccessfully() throws Exception {
            OwnerResponse ownerResponse = new OwnerResponse(1L, "John", "Doe");
            PropertyPatchRequest propertyPatchRequest = new PropertyPatchRequest("Patched Title", null, null, new BigDecimal(1500000));
            PropertyResponse propertyResponse = new PropertyResponse(1L, "Patched Title", "123 Test St", "A lovely property", new BigDecimal(1500000), ownerResponse);

            when(propertyService.patchProperty(eq(1L), any(PropertyPatchRequest.class))).thenReturn(propertyResponse);

            mockMvc.perform(patch("/api/properties/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyPatchRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Patched Title"))
                    .andExpect(jsonPath("$.address").value("123 Test St"))
                    .andExpect(jsonPath("$.description").value("A lovely property"))
                    .andExpect(jsonPath("$.price").value(1500000))
                    .andExpect(jsonPath("$.owner.id").value(1L))
                    .andExpect(jsonPath("$.owner.firstName").value("John"))
                    .andExpect(jsonPath("$.owner.lastName").value("Doe"));
        }

        @Test
        void shouldReturnNotFoundForNonExistentProperty() throws Exception {
            PropertyPatchRequest propertyPatchRequest = new PropertyPatchRequest("Patched Title", null, null, new BigDecimal(1500000));

            when(propertyService.patchProperty(eq(1L), any(PropertyPatchRequest.class))).thenThrow(new ResourceNotFoundException("Property Not Found"));

            mockMvc.perform(patch("/api/properties/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyPatchRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Property Not Found"));
        }
        @Test
        void shouldReturnBadRequestForInvalidInput() throws Exception {
            PropertyPatchRequest propertyPatchRequest = new PropertyPatchRequest(null, null, null, new BigDecimal(5000));

            mockMvc.perform(patch("/api/properties/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(propertyPatchRequest)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(propertyService);
        }
    }

    @Nested
    class DeletePropertyTests {
        @Test
        void shouldDeletePropertySuccessfully() throws Exception {

            mockMvc.perform(delete("/api/properties/1"))
                    .andExpect(status().isNoContent());
            verify(propertyService).deleteById(1L);
        }

        @Test
        void shouldReturnNotFoundForNonExistentProperty() throws Exception {
            doThrow(new ResourceNotFoundException("Property Not Found")).when(propertyService).deleteById(1L);

            mockMvc.perform(delete("/api/properties/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Property Not Found"));
        }
    }

    @Nested
    class GetPropertyByIdTests{
        @Test
        void shouldReturnPropertySuccessfully() throws Exception {
            OwnerResponse ownerResponse = new OwnerResponse(1L, "John", "Doe");
            PropertyResponse propertyResponse = new PropertyResponse(1L, "Title", "123 Test St", "A lovely property", BigDecimal.valueOf(1_500_000), ownerResponse);
            when(propertyService.getPropertyById(1L)).thenReturn(propertyResponse);

            mockMvc.perform(get("/api/properties/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("Title"))
                    .andExpect(jsonPath("$.owner.firstName").value("John"));
        }

        @Test
        void shouldReturnNotFoundForNonExistentProperty() throws Exception {
            when(propertyService.getPropertyById(1L)).thenThrow(new ResourceNotFoundException("Property Not Found"));

            mockMvc.perform(get("/api/properties/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Property Not Found"));
        }
    }

    @Nested
    class GetAllPropertiesTests {
        @Test
        void shouldGetPropertiesSuccessfully() throws Exception {
            OwnerResponse ownerResponse = new OwnerResponse(1L, "John", "Doe");
            OwnerResponse ownerResponse2 = new OwnerResponse(2L, "Jane", "Smith");
            PropertyResponse propertyResponse = new PropertyResponse(1L, "Test Property", "123 Test St", "A lovely property", BigDecimal.valueOf(1_000_000), ownerResponse);
            PropertyResponse propertyResponse2 = new PropertyResponse(2L, "Test Property 2", "456 Test Ave", "Another lovely property", BigDecimal.valueOf(1_500_000), ownerResponse);
            PropertyResponse propertyResponse3 = new PropertyResponse(3L, "Test Property 3", "789 Test Blvd", "Yet another lovely property", BigDecimal.valueOf(2_000_000), ownerResponse2);
            List<PropertyResponse> propertyResponseList = List.of(propertyResponse, propertyResponse2, propertyResponse3);
            when(propertyService.getAllProperties()).thenReturn(propertyResponseList);

            mockMvc.perform(get("/api/properties/allProperties"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(3))
                    .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3)));
        }

        @Test
        void shouldReturnEmptyList() throws Exception {
            when(propertyService.getAllProperties()).thenReturn(List.of());

            mockMvc.perform(get("/api/properties/allProperties"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

        }
    }

    @Nested
    class GetAllPropertiesByUserIdTests{

        @Test
        void shouldGetPropertiesByUserIdSuccessfully() throws Exception {
            OwnerResponse ownerResponse = new OwnerResponse(1L, "John", "Doe");
            PropertyResponse propertyResponse = new PropertyResponse(1L, "Test Property", "123 Test St", "A lovely property", BigDecimal.valueOf(1_000_000), ownerResponse);
            PropertyResponse propertyResponse2 = new PropertyResponse(2L, "Test Property 2", "456 Test Ave", "Another lovely property", BigDecimal.valueOf(1_500_000), ownerResponse);
            List<PropertyResponse> propertyResponseList = List.of(propertyResponse, propertyResponse2);

            when(propertyService.getPropertyByOwnerId(1L)).thenReturn(propertyResponseList);

            mockMvc.perform(get("/api/properties/1/allProperties"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[*].id" , containsInAnyOrder(1, 2)));
        }

        @Test
        void shouldReturnNotFoundForNonExistentUser() throws Exception {
            when(propertyService.getPropertyByOwnerId(1L)).thenThrow(new ResourceNotFoundException("User Not Found"));

            mockMvc.perform(get("/api/properties/1/allProperties"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User Not Found"));
        }

        @Test
        void shouldReturnEmptyList() throws Exception {
            when(propertyService.getPropertyByOwnerId(1L)).thenReturn(List.of());

            mockMvc.perform(get("/api/properties/1/allProperties"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }

    }

}