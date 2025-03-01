package com.example.soloTest.dto.response;

import com.example.soloTest.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private UUID orderId;
    private UUID userId;
    private OrderStatus status;
    private Double totalPrice;
    private List<OrderItemResponse> orderItems;
}
