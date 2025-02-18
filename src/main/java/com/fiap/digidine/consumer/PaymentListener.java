package com.fiap.digidine.consumer;

import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.dto.PaymentRequestDTO;
import com.fiap.digidine.mapper.OrderMapper;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderService orderService;

    OrderMapper orderMapper;

    @Value(value = "${digidine.broker.exchange.productionNotificationExchange}")
    public String productionNotificationExchange;

    @Value(value = "${digidine.broker.key.productionNotificationKey}")
    public String productionNotificationKey;

    @RabbitListener(queues = "${digidine.broker.queue.payment}")
    public void receiveOrder(PaymentRequestDTO paymentRequest) {
        // Processa o pagamento
        OrderResponseDTO orderResponseDTO = orderService.processOrder(
                paymentRequest.orderNumber());

        // Publica a mensagem na fila de pedidos
        rabbitTemplate.convertAndSend(productionNotificationExchange, productionNotificationKey, orderResponseDTO);
    }
}

