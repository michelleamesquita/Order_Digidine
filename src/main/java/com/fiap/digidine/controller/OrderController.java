package com.fiap.digidine.controller;

import com.fiap.digidine.dto.OrderRequestDTO;
import com.fiap.digidine.dto.OrderResponseDTO;
import com.fiap.digidine.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO request) {
        log.debug("POST order received {}", request);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(request));
        }catch (IllegalArgumentException illegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDTO> update(@PathVariable long orderNumber, @RequestBody OrderRequestDTO request) {
        log.debug("PUT order received {}", orderNumber, request);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderByOrderNumber(orderNumber, request));
        }catch (IllegalArgumentException illegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> list() {
        log.debug("GET list order received");
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.listOrders());
        }catch (IllegalArgumentException illegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable long orderNumber) {
        log.debug("GET order received {}", orderNumber);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getByOrderNumber(orderNumber));
        }catch (IllegalArgumentException illegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@PathVariable long orderNumber) {
        log.debug("DELETE order received {}", orderNumber);
        try{
            orderService.delete(orderNumber);
            return ResponseEntity.status(HttpStatus.OK).body("Pedido deletado com sucesso!");
        }catch (IllegalArgumentException illegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}