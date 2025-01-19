package com.fiap.digidine.dto;

import com.fiap.digidine.dto.enums.ProductCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequestDTO(
        @NotNull Long productNumber,
        @NotNull String name,
        @NotNull @PositiveOrZero Double price,
        ProductCategory category
) {}
