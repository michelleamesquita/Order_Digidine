package com.fiap.digidine.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

public record ProductRequestDTO(
        @NotNull Long productNumber,
        @NotNull String name,
        @NotNull @PositiveOrZero Double price,
        String category
) {}
