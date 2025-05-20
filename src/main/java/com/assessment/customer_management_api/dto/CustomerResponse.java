package com.assessment.customer_management_api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerResponse extends CustomerRequest {
    private UUID id;
    private String tier;
}