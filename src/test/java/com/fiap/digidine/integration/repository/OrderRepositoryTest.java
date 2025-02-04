package com.fiap.digidine.integration.repository;

import com.fiap.digidine.config.TestMongoConfig;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@Import(TestMongoConfig.class) // Importa a configuração correta
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void findByOrderNumber_ShouldReturnNullWhenNotFound() {
        // Act
        Order result = orderRepository.findByOrderNumber(9999L);

        // Assert
        assertThat(result).isNull();
    }
}
