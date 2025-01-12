package com.fiap.digidine.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderNumber,
        CustomerRequestDTO customer,
        List<ProductRequestDTO> products,
        double totalPrice,
        String orderStatus,
        LocalDateTime createdAt){}
