package com.fiap.digidine.consumer;

import com.fiap.digidine.dto.*;
import com.fiap.digidine.mapper.OrderMapper;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderUpdateListener {

    @Autowired
    OrderService orderService;

    OrderMapper orderMapper;

//    @RabbitListener(queues = "${digidine.broker.queue.orderUpdate}")
//    public void receiveOrderUpdate(OrderDTO orderDTO) {
//
//        OrderResponseDTO order = orderService.getByOrderNumber(orderDTO.orderNumber());
//
//        OrderResponseDTO orderUpdate = new OrderResponseDTO(
//                order.orderNumber(),
//                order.customer(),
//                order.products(),
//                order.totalPrice(),
//                orderDTO.orderStatus(),
//                order.createdAt()
//        );
//
//        orderService.updateOrderByOrderNumber(orderDTO.orderNumber(), orderUpdate);
//    }
}
