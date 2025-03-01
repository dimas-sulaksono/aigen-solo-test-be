package com.example.soloTest.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {
    private Long id;
    private UUID orderId;
    private Long productId;
    private Double price;
    private int quantity;
}
