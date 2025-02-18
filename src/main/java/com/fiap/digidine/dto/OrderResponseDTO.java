package com.fiap.digidine.dto;

import com.fiap.digidine.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderNumber,
        CustomerRequestDTO customer,
        List<ProductRequestDTO> products,
        double totalPrice,
        OrderStatus orderStatus,
        LocalDateTime createdAt){}
