package com.customer.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.customer.demo.dao.CustomerRepository;
import com.customer.demo.model.Customer;

@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @Autowired
    private CustomerRepository repository;

    @Test
    void testCreateAndGetById() {
        Customer customer = new Customer(null, "lee", "lee@google.com", 1200.0, LocalDateTime.now());
        Customer saved = service.create(customer);

        Customer fetched = service.getById(saved.getId());
        assertEquals("lee", fetched.getName());
    }

    @Test
    void testCalculateTierPlatinum() {
        Customer customer = new Customer(null, "Sagar", "Sagar@gmail.com", 12000.0, LocalDateTime.now().minusMonths(2));
        String tier = service.calculateTier(customer);
        assertEquals("Platinum", tier);
    }
    
    @Test
    void testCreateAndGetByIdNotFound() {
        Customer customer = new Customer(null, "lee", "lee@google.com", 1200.0, LocalDateTime.now());
        Customer saved = service.create(customer);
        
        Customer fetched = service.getById(saved.getId());
        assertNotEquals("Sagar", fetched.getName());
    }

    @Test
    void testCalculateTierPlatinumNegative() {
        Customer customer = new Customer(null, "Sagar", "Sagar@gmail.com", 12000.0, LocalDateTime.now().minusMonths(2));
        String tier = service.calculateTier(customer);
        assertNotEquals("Silver", tier);
    }
}
