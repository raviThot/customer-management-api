package com.assessment.customer_management_api.controller;

import com.assessment.customer_management_api.dto.CustomerRequest;
import com.assessment.customer_management_api.dto.CustomerResponse;
import com.assessment.customer_management_api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing customers.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    /**
     * Creates a new customer.
     *
     * @param request customer data
     * @return created customer with ID and tier
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request) {
        log.info("POST /customers - Creating customer: {}", request.getEmail());
        return ResponseEntity.status(201).body(service.create(request));
    }

    /**
     * Retrieves a customer by their unique ID.
     *
     * @param id customer UUID
     * @return customer details if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable UUID id) {
        log.info("GET /customers/{} - Retrieving by ID", id);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Customer with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Retrieves a customer by their name.
     *
     * @param name customer name
     * @return customer details if found
     */
    @GetMapping(params = "name")
    public ResponseEntity<CustomerResponse> getByName(@RequestParam String name) {
        log.info("GET /customers?name={} - Retrieving by name", name);
        return service.findByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Customer with name '{}' not found", name);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Retrieves a customer by their email.
     *
     * @param email customer email
     * @return customer details if found
     */
    @GetMapping(params = "email")
    public ResponseEntity<CustomerResponse> getByEmail(@RequestParam String email) {
        log.info("GET /customers?email={} - Retrieving by email", email);
        return service.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Customer with email '{}' not found", email);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Updates a customer's information.
     *
     * @param id      customer UUID
     * @param request updated customer data
     * @return updated customer if found
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable UUID id, @RequestBody @Valid CustomerRequest request) {
        log.info("PUT /customers/{} - Updating customer", id);
        return service.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Customer with ID {} not found for update", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id customer UUID
     * @return 204 No Content if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /customers/{} - Deleting customer", id);
        boolean deleted = service.delete(id);
        if (deleted) {
            log.debug("Customer with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Customer with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}
