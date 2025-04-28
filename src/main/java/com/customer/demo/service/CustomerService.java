package com.customer.demo.service;

import com.customer.demo.model.Customer;
import com.customer.demo.dao.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CustomerService {
	private final CustomerRepository repository;

	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public Customer create(Customer customer) {
		customer.setId(null);
		return repository.save(customer);
	}

	public Customer getById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Customer not found"));
	}

	public Optional<Customer> getByName(String name) {
		return repository.findByName(name);
	}

	public Optional<Customer> getByEmail(String email) {
		return repository.findByEmail(email);
	}

	public Customer update(UUID id, Customer updated) {
		Customer customer = getById(id);
		customer.setName(updated.getName());
		customer.setEmail(updated.getEmail());
		customer.setAnnualSpend(updated.getAnnualSpend());
		customer.setLastPurchaseDate(updated.getLastPurchaseDate());
		return repository.save(customer);
	}

	public void delete(UUID id) {
		repository.deleteById(id);
	}

	public String calculateTier(Customer customer) {
		Double spend = customer.getAnnualSpend();
		LocalDateTime last = customer.getLastPurchaseDate();
		if (spend == null || spend < 1000)
			return "Silver";
		if (spend >= 10000 && last != null && ChronoUnit.MONTHS.between(last, LocalDateTime.now()) <= 6)
			return "Platinum";
		if (spend >= 1000 && spend < 10000 && last != null
				&& ChronoUnit.MONTHS.between(last, LocalDateTime.now()) <= 12)
			return "Gold";
		return "Silver";
	}
}