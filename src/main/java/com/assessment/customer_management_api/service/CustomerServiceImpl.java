package com.assessment.customer_management_api.service;

import com.assessment.customer_management_api.dto.CustomerRequest;
import com.assessment.customer_management_api.dto.CustomerResponse;
import com.assessment.customer_management_api.model.Customer;
import com.assessment.customer_management_api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for customer management logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    /**
     * Converts a Customer entity to a CustomerResponse DTO with tier.
     */
    public CustomerResponse toResponse(Customer entity) {
        CustomerResponse response = new CustomerResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setAnnualSpend(entity.getAnnualSpend());
        response.setLastPurchaseDate(entity.getLastPurchaseDate());
        response.setTier(calculateTier(entity));
        return response;
    }

    /**
     * Calculates the tier for a customer based on annual spend and recency of purchase.
     */
    public String calculateTier(Customer customer) {
        BigDecimal spend = customer.getAnnualSpend() != null ? customer.getAnnualSpend() : BigDecimal.ZERO;
        LocalDateTime lastPurchase = customer.getLastPurchaseDate();

        if (spend.compareTo(BigDecimal.valueOf(10000)) >= 0 && lastPurchase != null &&
                lastPurchase.isAfter(LocalDateTime.now().minus(6, ChronoUnit.MONTHS))) {
            return "Platinum";
        } else if (spend.compareTo(BigDecimal.valueOf(1000)) >= 0 && lastPurchase != null &&
                lastPurchase.isAfter(LocalDateTime.now().minus(12, ChronoUnit.MONTHS))) {
            return "Gold";
        } else {
            return "Silver";
        }
    }

    /**
     * Creates a new customer record.
     */
    public CustomerResponse create(CustomerRequest request) {
        log.info("Creating new customer: {}", request.getEmail());
        Customer entity = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .annualSpend(request.getAnnualSpend())
                .lastPurchaseDate(request.getLastPurchaseDate())
                .build();
        Customer saved = repository.save(entity);
        log.debug("Customer created with ID: {}", saved.getId());
        return toResponse(saved);
    }

    /**
     * Finds a customer by ID.
     */
    public Optional<CustomerResponse> findById(UUID id) {
        log.debug("Fetching customer by ID: {}", id);
        return repository.findById(id).map(this::toResponse);
    }

    /**
     * Finds a customer by name.
     */
    public Optional<CustomerResponse> findByName(String name) {
        log.debug("Fetching customer by name: {}", name);
        return repository.findByName(name).map(this::toResponse);
    }

    /**
     * Finds a customer by email.
     */
    public Optional<CustomerResponse> findByEmail(String email) {
        log.debug("Fetching customer by email: {}", email);
        return repository.findByEmail(email).map(this::toResponse);
    }

    /**
     * Updates an existing customer if found.
     */
    public Optional<CustomerResponse> update(UUID id, CustomerRequest request) {
        log.info("Updating customer ID: {}", id);
        return repository.findById(id).map(existing -> {
            existing.setName(request.getName());
            existing.setEmail(request.getEmail());
            existing.setAnnualSpend(request.getAnnualSpend());
            existing.setLastPurchaseDate(request.getLastPurchaseDate());
            Customer updated = repository.save(existing);
            log.debug("Customer updated: {}", updated.getId());
            return toResponse(updated);
        });
    }

    /**
     * Deletes a customer by ID.
     */
    public boolean delete(UUID id) {
        log.info("Attempting to delete customer ID: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.debug("Customer deleted: {}", id);
            return true;
        }
        log.warn("Customer with ID {} not found for deletion", id);
        return false;
    }
}
