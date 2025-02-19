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
        order.setOrderId(1L);
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