package com.fiap.digidine.dto;

import java.time.LocalDateTime;

public record PaymentRequestDTO(
        Long orderNumber,
        double amount,
        LocalDateTime createdAt,
        String status
) {
}
