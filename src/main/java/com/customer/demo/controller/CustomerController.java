package com.customer.demo.controller;

import com.customer.demo.model.Customer;
import com.customer.demo.service.CustomerService;
import com.customer.demo.dto.CustomerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created",
                         content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CustomerRequest customerRequest) {
    	 	Customer customer = new Customer();
    	    customer.setName(customerRequest.getName());
    	    customer.setEmail(customerRequest.getEmail());
    	    customer.setAnnualSpend(customerRequest.getAnnualSpend());
    	    customer.setLastPurchaseDate(customerRequest.getLastPurchaseDate());

    	    Customer saved = service.create(customer);
    	    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                         content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(
            @Parameter(description = "UUID of the customer to retrieve") @PathVariable UUID id) {
        Customer customer = service.getById(id);
        return ResponseEntity.ok(toResponse(customer));
    }

    @Operation(summary = "Get customer by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                         content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getByName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {

        // If name is provided, fetch by name
        if (name != null) {
            return service.getByName(name)
                    .map(c -> ResponseEntity.ok(toResponse(c)))
                    .orElse(ResponseEntity.notFound().build());
        }
        // If email is provided, fetch by email
        else if (email != null) {
            return service.getByEmail(email)
                    .map(c -> ResponseEntity.ok(toResponse(c)))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Name or email must be provided"));
        }
    }

    @Operation(summary = "Update customer by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID id, @Valid @RequestBody Customer customer) {
        Customer updated = service.update(id, customer);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Operation(summary = "Delete customer by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toResponse(Customer customer) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", customer.getId());
        map.put("name", customer.getName());
        map.put("email", customer.getEmail());
        map.put("annualSpend", customer.getAnnualSpend());
        map.put("lastPurchaseDate", customer.getLastPurchaseDate());
        map.put("tier", service.calculateTier(customer));
        return map;
    }
}
