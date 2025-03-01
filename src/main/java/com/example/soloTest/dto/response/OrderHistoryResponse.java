package com.example.soloTest.dto.response;

import com.example.soloTest.model.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderHistoryResponse {
    private Long id;
    private UUID orderId;
    private OrderStatus status;
    private LocalDateTime changedAt;
    private UUID changedBy;
}
