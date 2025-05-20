package com.assessment.customer_management_api.controller;


import com.assessment.customer_management_api.dto.CustomerRequest;
import com.assessment.customer_management_api.model.Customer;
import com.assessment.customer_management_api.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository repository;

    private Customer customer;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        customer = repository.save(Customer.builder()
                .name("Suryaraviteja")
                .email("Suryaraviteja@example.com")
                .annualSpend(BigDecimal.valueOf(1500))
                .lastPurchaseDate(LocalDateTime.now().minusMonths(3))
                .build());
    }

    @Test
    public void testGetCustomerById() throws Exception {
        mockMvc.perform(get("/api/v1/customers/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Suryaraviteja"))
                .andExpect(jsonPath("$.tier").value("Gold"));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setName("test");
        request.setEmail("test@example.com");
        request.setAnnualSpend(BigDecimal.valueOf(11000));
        request.setLastPurchaseDate(LocalDateTime.now().minusMonths(2));

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.tier").value("Platinum"));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        CustomerRequest update = new CustomerRequest();
        update.setName("Suryaraviteja Updated");
        update.setEmail("Suryaraviteja.updated@example.com");
        update.setAnnualSpend(BigDecimal.valueOf(5000));
        update.setLastPurchaseDate(LocalDateTime.now());

        mockMvc.perform(put("/api/v1/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Suryaraviteja Updated"))
                .andExpect(jsonPath("$.tier").value("Gold"));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/" + customer.getId()))
                .andExpect(status().isNoContent());
    }
}
