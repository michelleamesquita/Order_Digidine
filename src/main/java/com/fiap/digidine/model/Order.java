package com.fiap.digidine.model;

import com.fiap.digidine.dto.CustomerRequestDTO;
import com.fiap.digidine.dto.ProductRequestDTO;
import com.fiap.digidine.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
@Document(collection = "orders")
public class Order {

    @MongoId
    private UUID orderUUID;
    private Long orderNumber;
    private CustomerRequestDTO customer;
    private List<ProductRequestDTO> products;
    private double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

}
