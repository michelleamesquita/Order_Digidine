package com.fiap.digidine.dto;

import org.springframework.http.HttpStatus;

public record NotificationDTO (
    String message,
    HttpStatus httpStatus,
    OrderResponseDTO orderResponseDTO)
{}
