package com.fiap.digidine.dto;

import com.fiap.digidine.model.enums.OrderStatus;

public record OrderDTO(Long orderNumber, OrderStatus orderStatus) {
}
