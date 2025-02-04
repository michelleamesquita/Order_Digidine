package com.fiap.digidine.bdd.cucumber;

import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.dto.ProductRequestDTO;
import com.fiap.digidine.dto.CustomerRequestDTO;
import com.fiap.digidine.dto.enums.ProductCategory;
import com.fiap.digidine.model.Order;
import com.fiap.digidine.model.enums.OrderStatus;
import com.fiap.digidine.repository.OrderRepository;
import com.fiap.digidine.service.impl.OrderServiceImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class OrderServiceSteps {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderRepository orderRepository;

    private OrderRequestDTO orderRequest;
    private OrderResponseDTO orderResponse;
    private List<OrderResponseDTO> orderList;
    private CustomerRequestDTO customerRequest;

    @Given("a valid OrderRequestDTO")
    public void aValidOrderRequestDTO() {
        customerRequest = new CustomerRequestDTO(1L, "John Doe");
        orderRequest = new OrderRequestDTO(
                customerRequest,
                List.of(new ProductRequestDTO(10L, "prod123", 10.99, Mockito.any(ProductCategory.class)))
        );
    }

    @When("the createOrder method is called")
    public void theCreateOrderMethodIsCalled() {
        orderResponse = orderService.createOrder(orderRequest);
    }

    @Then("an OrderResponseDTO should be returned")
    public void anOrderResponseDTOShouldBeReturned() {
        Assertions.assertNotNull(orderResponse);
        Assertions.assertEquals(orderRequest.customer(), orderResponse.customer());
    }

    @Then("the order should be saved in the repository")
    public void theOrderShouldBeSavedInTheRepository() {
        Order savedOrder = orderRepository.findByOrderNumber(orderResponse.orderNumber());
        Assertions.assertNotNull(savedOrder);
    }

    @Given("an order with orderNumber {int} exists in the repository")
    public void anOrderWithOrderNumberExistsInTheRepository(Long orderNumber) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setStatus(OrderStatus.RECEBIDO);
        orderRepository.save(order);
    }

    @When("the updateOrderStatusByOrderNumber method is called with status {string}")
    public void theUpdateOrderStatusByOrderNumberMethodIsCalledWithStatus(String status) {
        orderResponse = orderService.updateOrderStatusByOrderNumber(1L, OrderStatus.valueOf(status));
    }

    @Then("the order status should be updated in the repository")
    public void theOrderStatusShouldBeUpdatedInTheRepository() {
        Order updatedOrder = orderRepository.findByOrderNumber(1L);
        Assertions.assertEquals(OrderStatus.PRONTO, updatedOrder.getStatus());
    }

    @Then("the updated OrderResponseDTO should be returned")
    public void theUpdatedOrderResponseDTOShouldBeReturned() {
        Assertions.assertEquals(OrderStatus.PRONTO, orderResponse.orderStatus());
    }

    @Given("there are orders in the repository with different statuses")
    public void thereAreOrdersInTheRepositoryWithDifferentStatuses() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setOrderNumber(1L);
        order1.setCustomer(new CustomerRequestDTO(1L, "John Doe"));
        order1.setStatus(OrderStatus.RECEBIDO);
        order1.setProducts(List.of());
        order1.setTotalPrice(10.99);
        order1.setCreatedAt(null);

        Order order2 = new Order();
        order2.setOrderNumber(2L);
        order2.setCustomer(new CustomerRequestDTO(1L, "John Doe"));
        order2.setStatus(OrderStatus.PRONTO);
        order2.setProducts(List.of());
        order2.setTotalPrice(20.99);
        order2.setCreatedAt(null);

        orders.add(order1);
        orders.add(order2);
        orderRepository.saveAll(orders);
    }

    @When("the listOrders method is called")
    public void theListOrdersMethodIsCalled() {
        orderList = orderService.listOrders();
    }

    @Then("a sorted list of orders should be returned")
    public void aSortedListOfOrdersShouldBeReturned() {
        Assertions.assertNotNull(orderList);
        Assertions.assertEquals(OrderStatus.PRONTO, orderList.getFirst().orderStatus());
    }

    @When("the delete method is called with orderNumber {int}")
    public void theDeleteMethodIsCalledWithOrderNumber(int orderNumber) {
        orderService.delete((long) orderNumber);
    }

    @Then("the order should be removed from the repository")
    public void theOrderShouldBeRemovedFromTheRepository() {
        Order deletedOrder = orderRepository.findByOrderNumber(1L);
        Assertions.assertNull(orderRepository.findByOrderNumber(1L));
    }
}

