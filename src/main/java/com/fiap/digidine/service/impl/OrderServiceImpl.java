package com.fiap.digidine.service.impl;

import com.fiap.digidine.dto.NotificationDTO;
import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.dto.ProductRequestDTO;
import com.fiap.digidine.mapper.OrderMapper;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.model.enums.OrderStatus;
import com.fiap.digidine.producer.NotificationPublisher;
import com.fiap.digidine.repository.OrderRepository;
import com.fiap.digidine.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationPublisher notificationPublisher;

    @Autowired
    private OrderMapper mapper;

    @Transactional
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order baseOrder = buildOrderFromRequest(orderRequestDTO);

        Order savedOrder = orderRepository.save(baseOrder);

        if (savedOrder != null) {
            try {
                notificationPublisher.publishNotificationCommand(new NotificationDTO(
                        "Order created: " + savedOrder.getOrderNumber(),
                        HttpStatus.OK,
                        mapper.toOrderResponse(savedOrder)));
            } catch (Exception e) {
                log.warn("Error sending notification!");
            }
        }

        return mapper.toOrderResponse(savedOrder);
    }

    @Transactional
    @Override
    public OrderResponseDTO updateOrderByOrderNumber ( long orderNumber, OrderRequestDTO orderRequestDto){
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if (order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        order.setCustomer(orderRequestDto.customer());
        order.setProducts(orderRequestDto.products());
        order.setTotalPrice(calculateTotalOrderPrice(orderRequestDto.products()));

        orderRepository.save(order);

        if(order != null) {
            try {
                notificationPublisher.publishNotificationCommand(new NotificationDTO(
                        "Order updated: " + order.getOrderNumber(),
                        HttpStatus.OK,
                        mapper.toOrderResponse(order)));
            }
            catch (Exception e) {
                log.warn("Error sending notification!");
            }
        }
        return mapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponseDTO> listOrders () {
        List<Order> orders = orderRepository.findByStatusNotOrderByStatusAscCreatedAtAsc("Finalizado");

        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("Nenhum pedido encontrado.");
        }

        PriorityQueue<Order> priorityQueue = new PriorityQueue<>(Comparator
                .comparingInt(this::getStatusPriority)
                .thenComparing(Order::getCreatedAt));

        priorityQueue.addAll(orders);

        List<OrderResponseDTO> result = new ArrayList<>();

        while (!priorityQueue.isEmpty()) {
            result.add(mapper.toOrderResponse(priorityQueue.poll()));
        }

        return result;
    }

    @Transactional
    @Override
    public String getOrderStatus ( long orderNumber){
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if (order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        return order.getStatus().toString();
    }

    @Transactional
    @Override
    public void delete (Long orderNumber){
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if (order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        orderRepository.deleteById(order.getOrderId());

        try {
            notificationPublisher.publishNotificationCommand(new NotificationDTO(
                    "Order deleted: " + order.getOrderNumber(),
                    HttpStatus.OK,
                    mapper.toOrderResponse(order)));
        }catch (Exception e) {
            log.warn("Error sending notification!");
        }
    }

    @Override
    public OrderResponseDTO getByOrderNumber (Long orderNumber){
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if (order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        return mapper.toOrderResponse(order);
    }

    private long getNextOrderNumber () {
        Order lastOrder = orderRepository.findFirstByOrderByOrderNumberDesc();
        return (lastOrder != null ? lastOrder.getOrderNumber() : 0) + 1;
    }

    private Double calculateTotalOrderPrice (List < ProductRequestDTO > products) {
        double totalPrice = 0.0;

        for (ProductRequestDTO product : products) {
            totalPrice += product.price();
        }
        return totalPrice;
    }

    private Order buildOrderFromRequest (OrderRequestDTO orderRequestDTO){
        Random random = new Random();

        Order order = new Order();
        order.setOrderId(random.nextLong());
        order.setStatus(OrderStatus.RECEBIDO);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderNumber(getNextOrderNumber());
        order.setCustomer(orderRequestDTO.customer());
        order.setProducts(orderRequestDTO.products());
        order.setTotalPrice(calculateTotalOrderPrice(orderRequestDTO.products()));
        return order;
    }

    /*
     * @return Retorna a prioridade de status de um pedido
     * @param order Pedido a ser verificado
     */
    private int getStatusPriority (Order order){
        switch (order.getStatus()) {
            case PRONTO:
                return 1;
            case EM_PREPARACAO:
                return 2;
            case RECEBIDO:
                return 3;
            default:
                return Integer.MAX_VALUE;
        }
    }
}
