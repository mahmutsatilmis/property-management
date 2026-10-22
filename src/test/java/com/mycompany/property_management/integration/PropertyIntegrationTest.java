package com.mycompany.property_management.integration;


import com.mycompany.property_management.dto.request.PropertyRequest;
import com.mycompany.property_management.entity.Property;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PropertyIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EntityManager entityManager;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreatePropertyAndSaveToDB() throws Exception {
        User owner = userRepository.save(new User(null, "TestName", "TestSurname", "testemail111@example.com", "password1234", "1234567890"));
        entityManager.flush();
        PropertyRequest propertyRequest = new PropertyRequest("TestTitle", "TestAddress", "TestDescription", BigDecimal.valueOf(1_000_000), owner.getId());

        MvcResult result = mockMvc.perform(post("/api/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(jsonPath("$.title").value("TestTitle"))
                .andExpect(jsonPath("$.owner.id").value(owner.getId()))
                .andExpect(jsonPath("$.address").value("TestAddress"))
                .andReturn();
        Long propertyId = objectMapper.readTree(result.getResponse().getContentAsString())
                        .get("id").asLong();

        entityManager.flush();
        entityManager.clear();

        Property savedProperty = propertyRepository.findById(propertyId).orElseThrow();

        String location = result.getResponse().getHeader("Location");
        assertThat(location).isNotNull();
        assertThat(URI.create(location).getPath())
                .isEqualTo("/api/properties/" + propertyId);
        assertThat(savedProperty.getTitle()).isEqualTo("TestTitle");
        assertThat(savedProperty.getAddress()).isEqualTo("TestAddress");
        assertThat(savedProperty.getDescription()).isEqualTo("TestDescription");
        assertThat(savedProperty.getOwner().getId()).isEqualTo(owner.getId());
        assertThat(savedProperty.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(1_000_000));
    }

}
