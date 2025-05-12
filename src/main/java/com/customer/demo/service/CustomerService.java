package com.customer.demo.service;

import com.customer.demo.model.Customer;
import com.customer.demo.util.CustomerConstants;
import com.customer.demo.dao.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
	private final CustomerRepository repository;

	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public Customer create(Customer customer) {
		LOGGER.info("Creating customer : {}", customer.getName());
		customer.setId(null);
		Customer newCustomer = repository.save(customer);
		LOGGER.info("New Customer Created {}", newCustomer);
		return newCustomer;
	}

	public Customer getById(UUID id) {
		LOGGER.info("Getting Details of customer by ID :- {}", id);
		return repository.findById(id).orElseThrow(() -> {
			LOGGER.warn("Customer with id {} not found... ", id);
			return new NoSuchElementException(CustomerConstants.MSG_CUSTOMER_NOT_FOUND);
		});
	}

	public Optional<Customer> getByName(String name) {
		LOGGER.info("Getting customer details by name {}", name);
		return repository.findByName(name);
	}

	public Optional<Customer> getByEmail(String email) {
		LOGGER.info("Getting customer details by email {}", email);
		return repository.findByEmail(email);
	}

	public Customer update(UUID id, Customer updated) {
		LOGGER.info("Updating customer with id {}", id);
		Customer customer = getById(id);
		customer.setName(updated.getName());
		customer.setEmail(updated.getEmail());
		customer.setAnnualSpend(updated.getAnnualSpend());
		customer.setLastPurchaseDate(updated.getLastPurchaseDate());
		Customer updatedCustomer = repository.save(customer);
		LOGGER.info("Customer updated ... {}", updatedCustomer);
		return updatedCustomer;
	}

	public void delete(UUID id) {
		LOGGER.info("Deleting customer with id {}", id);
		repository.deleteById(id);
		LOGGER.info("Customer with id {} deleted...", id);
	}

	public String calculateTier(Customer customer) {
		LOGGER.info("Calculating customer tier ...");
		Double spend = customer.getAnnualSpend();
		LocalDateTime last = customer.getLastPurchaseDate();
		if (spend == null || spend < 1000) {
			LOGGER.info("Customer tier is calculated as Silver");
			return CustomerConstants.TIER_SILVER;
		}
		if (spend >= 10000 && last != null && ChronoUnit.MONTHS.between(last, LocalDateTime.now()) <= 6) {
			LOGGER.info("Customer tier is calculated as Platinum");
			return CustomerConstants.TIER_PLATINUM;
		}
		if (spend >= 1000 && spend < 10000 && last != null
				&& ChronoUnit.MONTHS.between(last, LocalDateTime.now()) <= 12) {
			LOGGER.info("Customer tier is calculated as Golld");
			return CustomerConstants.TIER_GOLD;
		}
		LOGGER.info("Customer tier is calculated as Silver");
		return CustomerConstants.TIER_SILVER;
	}
}