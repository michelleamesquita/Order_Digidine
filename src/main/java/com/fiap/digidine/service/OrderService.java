package com.fiap.digidine.service;

import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.model.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO updateOrderByOrderNumber(long orderNumber, OrderRequestDTO orderRequestDTO);
    List<OrderResponseDTO> listOrders();
    String getOrderStatus(long orderNumber);
    void delete(Long orderNumber);
    OrderResponseDTO getByOrderNumber(Long orderNumber);
    OrderResponseDTO processOrder(long orderNumber);
    OrderResponseDTO updateOrderByOrderNumber ( long orderNumber, OrderResponseDTO orderResponseDTO);
}
