package com.fiap.digidine.integration.producer;

import com.fiap.digidine.dto.*;
import com.fiap.digidine.dto.enums.ProductCategory;
import com.fiap.digidine.producer.NotificationPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificationPublisher notificationPublisher;

    private OrderResponseDTO sampleResponse;
    private final String EXCHANGE = "digidine.order.notification";
    private final String ROUTING_KEY = "notification-key";

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
    void publishNotification_ShouldSendMessageWithCorrectParameters() {
        // Arrange
        NotificationDTO notification = new NotificationDTO("123", HttpStatus.OK, sampleResponse);
        notificationPublisher.orderNotificationExchange = EXCHANGE;
        notificationPublisher.orderNotificationKey = ROUTING_KEY;

        // Act
        notificationPublisher.publishNotificationCommand(notification);

        // Assert
        verify(rabbitTemplate).convertAndSend(
                eq(EXCHANGE),
                eq(ROUTING_KEY),
                eq(notification)
        );
    }
}