package com.assessment.customer_management_api.service;

import com.assessment.customer_management_api.dto.CustomerRequest;
import com.assessment.customer_management_api.dto.CustomerResponse;
import com.assessment.customer_management_api.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    public CustomerResponse create(CustomerRequest request);
    public boolean delete(UUID id);
    public Optional<CustomerResponse> findById(UUID id);
    public Optional<CustomerResponse> findByName(String name);
    public Optional<CustomerResponse> findByEmail(String email);
    public Optional<CustomerResponse> update(UUID id, CustomerRequest request);
    public String calculateTier(Customer customer);
}
