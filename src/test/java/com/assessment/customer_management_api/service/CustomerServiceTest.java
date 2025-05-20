
package com.assessment.customer_management_api.service;

import com.assessment.customer_management_api.dto.CustomerRequest;
import com.assessment.customer_management_api.dto.CustomerResponse;
import com.assessment.customer_management_api.model.Customer;
import com.assessment.customer_management_api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    private UUID id;
    private Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        customer = Customer.builder()
                .id(id)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .annualSpend(BigDecimal.valueOf(12000))
                .lastPurchaseDate(LocalDateTime.now().minusMonths(1))
                .build();
    }

    @Test
    public void testCalculateTierPlatinum() {
        String tier = service.calculateTier(customer);
        assertEquals("Platinum", tier);
    }

    @Test
    public void testCreateCustomer() {
        CustomerRequest request = new CustomerRequest();
        request.setName("Jane Doe");
        request.setEmail("jane.doe@example.com");
        request.setAnnualSpend(BigDecimal.valueOf(12000));
        request.setLastPurchaseDate(LocalDateTime.now().minusMonths(1));

        when(repository.save(any())).thenReturn(customer);

        CustomerResponse response = service.create(request);

        assertNotNull(response);
        assertEquals("Jane Doe", response.getName());
        assertEquals("Platinum", response.getTier());
    }

    @Test
    public void testFindById() {
        when(repository.findById(id)).thenReturn(Optional.of(customer));

        Optional<CustomerResponse> response = service.findById(id);

        assertTrue(response.isPresent());
        assertEquals("Jane Doe", response.get().getName());
    }

    @Test
    public void testDeleteSuccess() {
        when(repository.existsById(id)).thenReturn(true);
        boolean deleted = service.delete(id);
        assertTrue(deleted);
        verify(repository).deleteById(id);
    }

    @Test
    public void testDeleteNotFound() {
        when(repository.existsById(id)).thenReturn(false);
        boolean deleted = service.delete(id);
        assertFalse(deleted);
    }
}
