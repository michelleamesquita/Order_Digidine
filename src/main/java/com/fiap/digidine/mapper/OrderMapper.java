package com.fiap.digidine.mapper;

import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDTO toOrderResponse(Order order)
    {
        return new OrderResponseDTO(
                order.getOrderNumber(),
                order.getCustomer(),
                order.getProducts(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt());
    }

    public List<OrderResponseDTO> toOrdersResponse(List<Order> orders)
    {
        List<OrderResponseDTO> ordersResponseDTO = new ArrayList<>();
        for(Order order : orders)
        {
            OrderResponseDTO orderResponseDTO = new OrderResponseDTO(
                    order.getOrderNumber(),
                    order.getCustomer(),
                    order.getProducts(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    order.getCreatedAt());

            ordersResponseDTO.add(orderResponseDTO);
        }

        return ordersResponseDTO;
    }

}
