package com.fiap.digidine.service.impl;

import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.dto.ProductRequestDTO;
import com.fiap.digidine.mapper.OrderMapper;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.model.enums.OrderStatus;
import com.fiap.digidine.repository.OrderRepository;
import com.fiap.digidine.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper mapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order baseOrder = buildOrderFromRequest(orderRequestDTO);

        Order savedOrder = orderRepository.save(baseOrder);

        return mapper.toOrderResponse(savedOrder);
    }

    @Override
    public OrderResponseDTO updateOrderStatusByOrderNumber(long orderNumber, OrderStatus status) {
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if(order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        order.setOrderStatus(status);

        // Salvando a entidade atualizada
        orderRepository.save(order);

        return mapper.toOrderResponse(order);
    }

    @Override
    public OrderResponseDTO updateOrderByOrderNumber(long orderNumber, OrderRequestDTO orderRequestDto) {
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if(order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        order.setCustomer(orderRequestDto.customer());
        order.setProducts(orderRequestDto.products());
        order.setTotalPrice(calculateTotalOrderPrice(orderRequestDto.products()));

        orderRepository.save(order);

        return mapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponseDTO> listOrders() {
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

    @Override
    public OrderStatus getOrderStatus(long orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if(order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        return order.getOrderStatus();
    }

    @Override
    public void delete(Long orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if(order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        orderRepository.deleteById(order.getOrderUUID());
    }

    @Override
    public OrderResponseDTO getByOrderNumber(Long orderNumber){
        Order order = orderRepository.findByOrderNumber(orderNumber);

        if(order == null) {
            throw new IllegalArgumentException("Pedido não cadastrado anteriormente!");
        }

        return mapper.toOrderResponse(order);
    }

    private long getNextOrderNumber() {
        Order lastOrder = orderRepository.findFirstByOrderByOrderNumberDesc();
        return (lastOrder != null ? lastOrder.getOrderNumber() : 0) + 1;
    }

    private Double calculateTotalOrderPrice(List<ProductRequestDTO> products) {
        double totalPrice = 0.0;

        for (ProductRequestDTO product : products) {
            totalPrice += product.price();
        }
        return totalPrice;
    }

    private Order buildOrderFromRequest(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.RECEBIDO);
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
    private int getStatusPriority(Order order) {
        switch (order.getOrderStatus()) {
            case PRONTO: return 1;
            case EM_PREPARACAO: return 2;
            case RECEBIDO: return 3;
            default: return Integer.MAX_VALUE;
        }
    }
}
