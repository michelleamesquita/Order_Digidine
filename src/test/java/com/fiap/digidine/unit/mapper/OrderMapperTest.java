package com.fiap.digidine.unit.mapper;

import com.fiap.digidine.dto.CustomerRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.dto.ProductRequestDTO;
import com.fiap.digidine.dto.enums.ProductCategory;
import com.fiap.digidine.mapper.OrderMapper;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper mapper;
    private Order order;
    private final LocalDateTime testTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        mapper = new OrderMapper();

        order = new Order();
        order.setOrderUUID(UUID.randomUUID());
        order.setOrderNumber(12345L);
        order.setCustomer(new CustomerRequestDTO(1L, "Cliente Teste"));
        order.setProducts(List.of(
                new ProductRequestDTO(10L, "Hambúrguer", 25.90, ProductCategory.LANCHE),
                new ProductRequestDTO(20L, "Refrigerante", 8.50, ProductCategory.BEBIDA)
        ));
        order.setTotalPrice(34.40);
        order.setStatus(OrderStatus.EM_PREPARACAO);
        order.setCreatedAt(testTime);
    }

    @Test
    void toOrderResponse_ShouldMapAllFieldsCorrectly() {
        OrderResponseDTO dto = mapper.toOrderResponse(order);

        assertAll(
                () -> assertEquals(order.getOrderNumber(), dto.orderNumber()),
                () -> assertEquals(order.getCustomer(), dto.customer()),
                () -> assertEquals(order.getProducts(), dto.products()),
                () -> assertEquals(order.getTotalPrice(), dto.totalPrice()),
                () -> assertEquals(order.getStatus().toString(), dto.orderStatus()),
                () -> assertEquals(order.getCreatedAt(), dto.createdAt())
        );
    }

    @Test
    void toOrderResponse_ShouldHandleDifferentStatuses() {
        order.setStatus(OrderStatus.PRONTO);
        OrderResponseDTO dto = mapper.toOrderResponse(order);

        assertEquals("PRONTO", dto.orderStatus());
    }

    @Test
    void toOrdersResponse_ShouldConvertListProperly() {
        Order order2 = new Order();
        order2.setOrderNumber(67890L);
        order2.setStatus(OrderStatus.RECEBIDO);

        List<OrderResponseDTO> result = mapper.toOrdersResponse(List.of(order, order2));

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(12345L, result.get(0).orderNumber()),
                () -> assertEquals(67890L, result.get(1).orderNumber()),
                () -> assertEquals("EM_PREPARACAO", result.get(0).orderStatus()),
                () -> assertEquals("RECEBIDO", result.get(1).orderStatus())
        );
    }

    @Test
    void toOrdersResponse_ShouldHandleEmptyList() {
        List<OrderResponseDTO> result = mapper.toOrdersResponse(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void toOrderResponse_ShouldHandleNullProducts() {
        order.setProducts(null);
        OrderResponseDTO dto = mapper.toOrderResponse(order);

        assertNull(dto.products());
    }

    @Test
    void toOrderResponse_ShouldHandleNullCustomer() {
        order.setCustomer(null);
        OrderResponseDTO dto = mapper.toOrderResponse(order);

        assertNull(dto.customer());
    }
}