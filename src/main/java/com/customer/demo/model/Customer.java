package com.customer.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private Double annualSpend;
    private LocalDateTime lastPurchaseDate;
    
    public Customer() {
    	
    }
    
	public Customer(UUID id, @NotBlank(message = "Name is required") String name,
			@Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,
			Double annualSpend, LocalDateTime lastPurchaseDate) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.annualSpend = annualSpend;
		this.lastPurchaseDate = lastPurchaseDate;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Double getAnnualSpend() {
		return annualSpend;
	}
	public void setAnnualSpend(Double annualSpend) {
		this.annualSpend = annualSpend;
	}
	public LocalDateTime getLastPurchaseDate() {
		return lastPurchaseDate;
	}
	public void setLastPurchaseDate(LocalDateTime lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}
}

