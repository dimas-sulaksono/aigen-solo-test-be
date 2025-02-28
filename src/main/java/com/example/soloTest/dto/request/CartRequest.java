package com.example.soloTest.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CartRequest {
    private UUID userId;
    private Long productId;
    private int quantity;
}
