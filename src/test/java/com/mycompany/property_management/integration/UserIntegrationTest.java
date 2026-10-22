package com.mycompany.property_management.integration;

import com.mycompany.property_management.dto.request.UserRequest;
import com.mycompany.property_management.entity.Property;
import com.mycompany.property_management.entity.User;
import com.mycompany.property_management.repository.PropertyRepository;
import com.mycompany.property_management.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EntityManager entityManager;
    @Autowired
    private PropertyRepository propertyRepository;

    private UserRequest userRequest;

    @BeforeEach
    public void setup() {
        userRequest = new UserRequest("TestName", "TestSurname", "test999@example.com", "password1234", "1234567890");

    }

    @Test
    void shouldCreateUserAndSaveToDB() throws Exception {

        MvcResult result= mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(jsonPath("$.email").value("test999@example.com"))
                        .andReturn();
        Long userId = objectMapper.readTree(result.getResponse().getContentAsString())
                        .get("id").asLong();

        entityManager.flush();
        entityManager.clear();

        User savedUser = userRepository.findByEmail("test999@example.com").orElseThrow();

        String location = result.getResponse().getHeader("Location");
        assertThat(location).isNotNull();
        assertThat(URI.create(location).getPath())
                .isEqualTo("/api/users/" + userId);
        assertThat(savedUser.getId()).isEqualTo(userId);
        assertThat(savedUser.getFirstName()).isEqualTo("TestName");
        assertThat(savedUser.getLastName()).isEqualTo("TestSurname");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("1234567890");
    }

    @Test
    void shouldThrowDuplicateEmailException() throws Exception {

        userRepository.save(createUser("test999@example.com"));
        UserRequest userRequest2 = new UserRequest("TestName2", "TestSurname2", "test999@example.com", "password4321", "0987654321");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void shouldDeleteUserAndTheirProperties() throws Exception {

        User user1 = userRepository.save(createUser("test1000@example.com"));
        User user2 = userRepository.save(createUser("test1001@example.com"));
        propertyRepository.save(createProperty(user1));
        propertyRepository.save(createProperty(user1));
        propertyRepository.save(createProperty(user2));
        Long user1Id = user1.getId();
        Long user2Id = user2.getId();
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/api/users/"+user1Id))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        assertThat(propertyRepository.findAllByOwnerId(user2Id)).isNotEmpty() ;
        assertThat(propertyRepository.findAllByOwnerId(user1Id)).isEmpty();
        assertThat(userRepository.findById(user1Id)).isEmpty();
        assertThat(userRepository.findById(user2Id)).isPresent();

    }

    private User createUser(String email) {
        return new User(null, "TestName", "TestSurname", email, "password1234", "1234567890");
    }

    private Property createProperty(User owner) {
        return new Property(null, "TestProperty1", "TestAddress1", "TestDescription1", BigDecimal.valueOf(1_000_000), owner);
    }

}
