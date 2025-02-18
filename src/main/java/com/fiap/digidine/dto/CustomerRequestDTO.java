package com.fiap.digidine.dto;

import jakarta.validation.constraints.NotNull;

public record CustomerRequestDTO (
        @NotNull(message = "Customer number is required")
        Long customerNumber,
        @NotNull(message = "Customer name is required")
        String name)
{}
