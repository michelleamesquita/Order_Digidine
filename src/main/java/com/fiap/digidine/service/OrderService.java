package com.fiap.digidine.service;

import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO updateOrderStatusByOrderNumber(long orderNumber, OrderStatus status);
    OrderResponseDTO updateOrderByOrderNumber(long orderNumber, OrderRequestDTO orderRequestDTO);
    List<OrderResponseDTO> listOrders();
    OrderStatus getOrderStatus(long orderNumber);
    void delete(Long orderNumber);
    OrderResponseDTO getByOrderNumber(Long orderNumber);
}
