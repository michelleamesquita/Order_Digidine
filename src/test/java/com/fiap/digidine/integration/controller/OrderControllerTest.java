package com.fiap.digidine.integration.controller;

import com.fiap.digidine.controller.OrderController;
import com.fiap.digidine.dto.CustomerRequestDTO;
import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.dto.ProductRequestDTO;
import com.fiap.digidine.dto.enums.ProductCategory;
import com.fiap.digidine.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderRequestDTO validRequest;
    private OrderResponseDTO sampleResponse;
    private final Long validOrderNumber = 1L;
    private final Long invalidOrderNumber = 999L;

    @BeforeEach
    void setUp() {
        var customerRequest = new CustomerRequestDTO(1L, "John Doe");

        // Configuração correta com valores concretos
        ProductRequestDTO productRequest = new ProductRequestDTO(
                10L,
                "prod123",
                10.99,
                ProductCategory.ACOMPANHAMENTO // Valor concreto
        );

        validRequest = new OrderRequestDTO(
                customerRequest,
                List.of(productRequest)
        );

        sampleResponse = new OrderResponseDTO(
                1L,
                customerRequest,
                List.of(productRequest),
                10.99,
                "EM_PREPARACAO",
                LocalDateTime.now()
        );
    }

    @Test
    void createOrder_WithValidRequest_ReturnsCreatedOrder() {
        when(orderService.createOrder(validRequest)).thenReturn(sampleResponse);

        ResponseEntity<Object> response = orderController.create(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
        verify(orderService).createOrder(validRequest);
    }

    @Test
    void createOrder_WithInvalidRequest_ReturnsBadRequest() {
        String errorMessage = "Dados inválidos";
        when(orderService.createOrder(validRequest)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<Object> response = orderController.create(validRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro: " + errorMessage, response.getBody());
    }

    @Test
    void updateOrder_WithValidData_ReturnsUpdatedOrder() {
        when(orderService.updateOrderByOrderNumber(validOrderNumber, validRequest)).thenReturn(sampleResponse);

        ResponseEntity<Object> response = orderController.updateByOrderNumber(validOrderNumber, validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
        verify(orderService).updateOrderByOrderNumber(validOrderNumber, validRequest);
    }

    @Test
    void updateOrder_WithInvalidOrderNumber_ReturnsNotFound() {
        String errorMessage = "Pedido não encontrado";
        when(orderService.updateOrderByOrderNumber(invalidOrderNumber, validRequest))
                .thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<Object> response = orderController.updateByOrderNumber(invalidOrderNumber, validRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erro: " + errorMessage, response.getBody());
    }

    @Test
    void listOrders_WhenOrdersExist_ReturnsOrderList() {
        List<OrderResponseDTO> orders = Arrays.asList(sampleResponse, sampleResponse);
        when(orderService.listOrders()).thenReturn(orders);

        ResponseEntity<List<Object>> response = orderController.list();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(orders), response.getBody());
    }

    @Test
    void getOrderById_WithValidOrderNumber_ReturnsOrder() {
        when(orderService.getByOrderNumber(validOrderNumber)).thenReturn(sampleResponse);

        ResponseEntity<Object> response = orderController.getOrderByOrderNumber(validOrderNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
    }

    @Test
    void getOrderById_WithInvalidOrderNumber_ReturnsOrder() {
        String errorMessage = "Pedido não encontrado";
        when(orderService.getByOrderNumber(invalidOrderNumber))
                .thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<Object> response = orderController.getOrderByOrderNumber(invalidOrderNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erro: " + errorMessage, response.getBody());
    }

    @Test
    void getOrderStatus_WithInvalidOrderNumber_ReturnsNotFound() {
        String errorMessage = "Pedido não encontrado";
        when(orderService.getOrderStatus(invalidOrderNumber))
                .thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<String> response = orderController.getOrderStatusByOrderNumber(invalidOrderNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erro: " + errorMessage, response.getBody());
    }

    @Test
    void getOrderStatus_WithValidOrderNumber_ReturnsStatus() {
        String expectedStatus = "EM_PREPARO";
        when(orderService.getOrderStatus(validOrderNumber)).thenReturn(expectedStatus);

        ResponseEntity<String> response = orderController.getOrderStatusByOrderNumber(validOrderNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStatus, response.getBody());
    }

    @Test
    @Transactional
    void deleteOrder_WithValidOrderNumber_ReturnsSuccessMessage() {
        doNothing().when(orderService).delete(validOrderNumber);

        ResponseEntity<String> response = orderController.delete(validOrderNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pedido deletado com sucesso!", response.getBody());
        verify(orderService).delete(validOrderNumber);
    }

    @Test
    @Transactional
    void deleteOrder_WithInvalidOrderNumber_ReturnsNotFound() {
        String errorMessage = "Pedido não encontrado";
        doThrow(new IllegalArgumentException(errorMessage)).when(orderService).delete(invalidOrderNumber);

        ResponseEntity<String> response = orderController.delete(invalidOrderNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erro: " + errorMessage, response.getBody());
    }
}