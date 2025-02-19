package com.fiap.digidine.unit.service;

import com.fiap.digidine.dto.*;
import com.fiap.digidine.dto.enums.ProductCategory;
import com.fiap.digidine.mapper.OrderMapper;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.model.enums.OrderStatus;
import com.fiap.digidine.producer.NotificationPublisher;
import com.fiap.digidine.repository.OrderRepository;
import com.fiap.digidine.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private NotificationPublisher notificationPublisher;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderRequestDTO requestDTO;
    private OrderResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderNumber(1L);
        order.setStatus(OrderStatus.RECEBIDO);

        requestDTO = new OrderRequestDTO(
                new CustomerRequestDTO(1L, "Cliente"),
                List.of(new ProductRequestDTO(1L, "Produto", 10.0, ProductCategory.LANCHE))
        );

        responseDTO = new OrderResponseDTO(
                1L,
                new CustomerRequestDTO(1L, "Cliente"),
                List.of(new ProductRequestDTO(1L, "Produto", 10.0, ProductCategory.LANCHE)),
                10.0,
                OrderStatus.RECEBIDO,
                LocalDateTime.now()
        );
    }

    @Test
    void createOrder_ShouldSaveAndReturnOrder() {
        when(orderRepository.save(any())).thenReturn(order);
        when(mapper.toOrderResponse(any())).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.createOrder(requestDTO);

        assertNotNull(result);
        verify(orderRepository).save(any());
        verify(notificationPublisher).publishNotificationCommand(any());
    }

    @Test
    void updateOrder_ShouldRecalculateTotalPrice() {
        List<ProductRequestDTO> newProducts = List.of(
                new ProductRequestDTO(2L, "Novo Produto", 15.0, ProductCategory.BEBIDA)
        );
        OrderRequestDTO updateRequest = new OrderRequestDTO(
                new CustomerRequestDTO(1L, "Cliente Atualizado"),
                newProducts
        );

        when(orderRepository.findByOrderNumber(1L)).thenReturn(order);
        when(orderRepository.save(any())).thenReturn(order);
        when(mapper.toOrderResponse(any())).thenReturn(responseDTO);

        orderService.updateOrderByOrderNumber(1L, updateRequest);

        assertEquals(15.0, order.getTotalPrice());
    }

    @Test
    void listOrders_ShouldReturnSortedOrders() {
        Order order1 = createTestOrder(1L, OrderStatus.RECEBIDO);
        Order order2 = createTestOrder(2L, OrderStatus.EM_PREPARACAO);

        when(orderRepository.findByStatusNotOrderByStatusAscCreatedAtAsc("Finalizado"))
                .thenReturn(List.of(order1, order2));
        when(mapper.toOrderResponse(any())).thenReturn(responseDTO);

        List<OrderResponseDTO> result = orderService.listOrders();

        assertEquals(2, result.size());
        assertEquals("RECEBIDO", result.get(0).orderStatus());
    }

    @Test
    void listOrders_ShouldThrowExceptionWhenNoOrders() {
        when(orderRepository.findByStatusNotOrderByStatusAscCreatedAtAsc("Finalizado"))
                .thenReturn(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () ->
                orderService.listOrders()
        );
    }

    @Test
    void deleteOrder_ShouldRemoveAndSendNotification() {
        when(orderRepository.findByOrderNumber(1L)).thenReturn(order);

        orderService.delete(1L);

        verify(orderRepository).deleteById(order.getOrderId());
        verify(notificationPublisher).publishNotificationCommand(any());
    }

    @Test
    void getOrderStatus_ShouldReturnCorrectStatus() {
        order.setStatus(OrderStatus.FINALIZADO);
        when(orderRepository.findByOrderNumber(1L)).thenReturn(order);

        String status = orderService.getOrderStatus(1L);

        assertEquals("FINALIZADO", status);
    }

    private Order createTestOrder(Long number, OrderStatus status) {
        Order pedido = new Order();
        pedido.setOrderNumber(number);
        pedido.setStatus(status);
        pedido.setCreatedAt(LocalDateTime.now());
        return pedido;
    }
}