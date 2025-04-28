package com.customer.demo.dao;
import com.customer.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

	public interface CustomerRepository extends JpaRepository<Customer, UUID> {
	    Optional<Customer> findByName(String name);
	    Optional<Customer> findByEmail(String email);
	}