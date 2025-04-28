package com.customer.demo.controller;

import com.customer.demo.model.Customer;
import com.customer.demo.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service; 

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID id = UUID.randomUUID();
    private final Customer sampleCustomer = new Customer(
            id, "Sagar", "sagar@customerservice.com", 5000.0, LocalDateTime.now().minusMonths(2)
    );

    // Test CRUD Operations

    @Test
    void testCreateCustomer() throws Exception {
        Mockito.when(service.create(any())).thenReturn(sampleCustomer);
        Mockito.when(service.calculateTier(any())).thenReturn("Gold");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sagar"))
                .andExpect(jsonPath("$.tier").value("Gold"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Mockito.when(service.getById(id)).thenReturn(sampleCustomer);
        Mockito.when(service.calculateTier(any())).thenReturn("Gold");

        mockMvc.perform(get("/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("sagar@customerservice.com"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Mockito.when(service.update(eq(id), any())).thenReturn(sampleCustomer);
        Mockito.when(service.calculateTier(any())).thenReturn("Gold");

        mockMvc.perform(put("/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sagar"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/{id}", id))
                .andExpect(status().isNoContent());
    }

    // TestCustomer Tier Calculations

    @Test
    void testCalculateTierForSilver() throws Exception {
        Customer customerSilver = new Customer(id, "Groot", "groot@customerservice.com", 500.0, LocalDateTime.now().minusMonths(2));
        Mockito.when(service.create(any())).thenReturn(customerSilver);
        Mockito.when(service.calculateTier(any())).thenReturn("Silver");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerSilver)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tier").value("Silver"));
    }

    @Test
    void testCalculateTierForGold() throws Exception {
        Customer customerGold = new Customer(id, "IronMan", "ironman@customerservice.com", 5000.0, LocalDateTime.now().minusMonths(8));
        Mockito.when(service.create(any())).thenReturn(customerGold);
        Mockito.when(service.calculateTier(any())).thenReturn("Gold");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerGold)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tier").value("Gold"));
    }

    @Test
    void testCalculateTierForPlatinum() throws Exception {
        Customer customerPlatinum = new Customer(id, "Thor", "thor@customerservice.com", 15000.0, LocalDateTime.now().minusMonths(4));
        Mockito.when(service.create(any())).thenReturn(customerPlatinum);
        Mockito.when(service.calculateTier(any())).thenReturn("Platinum");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerPlatinum)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tier").value("Platinum"));
    }

    // Test Email Id Validation

    @Test
    void testInvalidEmailFormat() throws Exception {
        Customer invalidEmailCustomer = new Customer(id, "Eve", "myInvalidEmailId", 3000.0, LocalDateTime.now());

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmailCustomer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage").value("Invalid email format"));
    }
}
