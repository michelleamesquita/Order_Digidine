package com.fiap.digidine.repository;

import com.fiap.digidine.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends MongoRepository<Order, UUID> {

    List<Order> findByStatusNotOrderByStatusAscCreatedAtAsc(String status);

    Order findFirstByOrderByOrderNumberDesc();

    Order findByOrderNumber(long orderNumber);
}
