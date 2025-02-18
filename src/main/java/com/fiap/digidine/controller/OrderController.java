package com.fiap.digidine.controller;

import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private final OrderService orderService;

    private static final String ERROR = "Erro: ";

    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody OrderRequestDTO request) {
        log.debug("POST order received {}", request);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(request));
        }catch (IllegalArgumentException illegalArgumentException) {
            log.error("Error while creating order: {}", illegalArgumentException.getMessage(), illegalArgumentException);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR + illegalArgumentException.getMessage());
        }
    }

    @PutMapping("/{orderNumber}")
    public ResponseEntity<Object> updateByOrderNumber(@PathVariable long orderNumber, @RequestBody OrderRequestDTO request) {
        log.debug("PUT order received {}", orderNumber, request);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderByOrderNumber(orderNumber, request));
        }catch (IllegalArgumentException illegalArgumentException) {
            log.error("Error while updating order: {}", illegalArgumentException.getMessage(), illegalArgumentException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ERROR + illegalArgumentException.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Object>> list() {
        log.debug("GET list order received");
        try{
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonList(orderService.listOrders()));
        }catch (IllegalArgumentException illegalArgumentException) {
            log.error("Error while listing order: {}", illegalArgumentException.getMessage(), illegalArgumentException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonList(ERROR + illegalArgumentException.getMessage()));
        }
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<Object> getOrderByOrderNumber(@PathVariable long orderNumber) {
        log.debug("GET order received {}", orderNumber);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getByOrderNumber(orderNumber));
        }catch (IllegalArgumentException illegalArgumentException) {
            log.error("Error while getting order: {}", illegalArgumentException.getMessage(), illegalArgumentException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ERROR + illegalArgumentException.getMessage());
        }
    }

    @GetMapping("/status/{orderNumber}")
    public ResponseEntity<String> getOrderStatusByOrderNumber(@PathVariable long orderNumber) {
        log.debug("GET order received {}", orderNumber);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderStatus(orderNumber));
        }catch (IllegalArgumentException illegalArgumentException) {
            log.error("Error while getting order: {}", illegalArgumentException.getMessage(), illegalArgumentException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ERROR + illegalArgumentException.getMessage());
        }
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<String> delete(@PathVariable long orderNumber) {
        log.debug("DELETE order received {}", orderNumber);
        try{
            orderService.delete(orderNumber);
            return ResponseEntity.status(HttpStatus.OK).body("Pedido deletado com sucesso!");
        }catch (IllegalArgumentException illegalArgumentException) {
            log.error("Error while deleting order: {}", illegalArgumentException.getMessage(), illegalArgumentException);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ERROR + illegalArgumentException.getMessage());
        }
    }
}