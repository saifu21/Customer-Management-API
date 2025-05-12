package com.customer.demo.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.customer.demo.model.Customer;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    void testFindByName() {
        Customer customer = new Customer(null, "Sam", "sam@google.com", 2500.0, LocalDateTime.now());
        repository.save(customer);

        Optional<Customer> result = repository.findByName("Sam");
        assertTrue(result.isPresent());
    }

    @Test
    void testFindByEmail() {
        Customer customer = new Customer(null, "Ramen", "ramen@jio.com", 3000.0, LocalDateTime.now());
        repository.save(customer);

        Optional<Customer> result = repository.findByEmail("ramen@jio.com");
        assertTrue(result.isPresent());
    }
    
    @Test
    void testFindByNameNotPresent() {
        Optional<Customer> result = repository.findByName("Alien");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByEmailNotPresent() {
        Optional<Customer> result = repository.findByEmail("alien@jio.com");
        assertTrue(result.isEmpty());
    }
}
