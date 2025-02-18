package com.fiap.digidine.model;

import com.fiap.digidine.dto.CustomerRequestDTO;
import com.fiap.digidine.dto.ProductRequestDTO;
import com.fiap.digidine.model.enums.OrderStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @MongoId
    private Long orderId;
    private Long orderNumber;
    private CustomerRequestDTO customer;
    private List<ProductRequestDTO> products;
    private double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

}
