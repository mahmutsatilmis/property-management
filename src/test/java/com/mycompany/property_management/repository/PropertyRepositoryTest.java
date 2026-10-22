package com.mycompany.property_management.repository;

import com.mycompany.property_management.entity.Property;
import com.mycompany.property_management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PropertyRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    void deleteAllByOwnerIdTest() {
        User owner1 = new User(null, "TestName1","TesLastName1","test999@example.com","password1","1234567890");
        User owner2 = new User(null, "TestName2","TesLastName2","test998@example.com","password2","1234567890");

        testEntityManager.persist(owner1);
        testEntityManager.persist(owner2);
        testEntityManager.flush();

        Property property1 = new Property(null, "TestTitle1", "Test Address1", "Test Description1", new BigDecimal(1000000),owner1);
        Property property2 = new Property(null, "TestTitle2", "Test Address2", "Test Description2", new BigDecimal(1000000),owner1);
        Property property3 = new Property(null, "TestTitle3", "Test Address3", "Test Description3", new BigDecimal(1000000),owner2);

        testEntityManager.persist(property1);
        testEntityManager.persist(property2);
        testEntityManager.persist(property3);
        testEntityManager.flush();

        propertyRepository.deleteAllByOwnerId(owner1.getId());
        testEntityManager.clear();

        assertThat(propertyRepository.findAllByOwnerId(owner1.getId())).isEmpty();
        assertThat(propertyRepository.findAllByOwnerId(owner2.getId())).isNotEmpty();
    }
}