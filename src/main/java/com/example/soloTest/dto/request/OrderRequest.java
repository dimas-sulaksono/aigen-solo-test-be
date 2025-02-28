package com.example.soloTest.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequest {
    private UUID userId;
    private String status;
}
