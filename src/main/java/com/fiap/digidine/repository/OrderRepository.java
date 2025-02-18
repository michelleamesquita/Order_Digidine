package com.fiap.digidine.repository;

import com.fiap.digidine.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableMongoRepositories
public interface OrderRepository extends MongoRepository<Order, Long> {

    List<Order> findByStatusNotOrderByStatusAscCreatedAtAsc(String status);

    Order findFirstByOrderByOrderNumberDesc();

    Order findByOrderNumber(long orderNumber);
}
