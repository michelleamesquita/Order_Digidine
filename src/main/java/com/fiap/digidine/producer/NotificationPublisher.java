package com.fiap.digidine.producer;

import com.fiap.digidine.dto.NotificationDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${digidine.broker.exchange.orderNotificationExchange}")
    public String orderNotificationExchange;

    @Value(value = "${digidine.broker.key.orderNotificationKey}")
    public String orderNotificationKey;

    public void publishNotificationCommand(NotificationDTO notificationDTO) {
        rabbitTemplate.convertAndSend(orderNotificationExchange, orderNotificationKey, notificationDTO);
    }
}
