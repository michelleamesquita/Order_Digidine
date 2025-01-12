package com.fiap.digidine.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO(
        @NotNull(message = "Customer is required")
        CustomerRequestDTO customer,

        @NotEmpty(message = "Products list cannot be empty")
        List<ProductRequestDTO> products
) {}

