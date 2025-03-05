package com.example.soloTest.controller;

import com.example.soloTest.dto.response.ApiResponse;
import com.example.soloTest.dto.response.PaginatedResponse;
import com.example.soloTest.dto.response.OrderHistoryResponse;
import com.example.soloTest.model.OrderHistory;
import com.example.soloTest.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-history")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    // Get all order history
    @GetMapping
    public ResponseEntity<?> getAllOrderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<OrderHistoryResponse> history = orderHistoryService.getAllOrderHistory(page, size);
            return ResponseEntity.ok(new PaginatedResponse<>(200, history));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Failed to retrieve order history: " + e.getMessage()));
        }
    }

    // Get order history by order id
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistoryByOrderId(@PathVariable UUID orderId) {
        List<OrderHistoryResponse> history = orderHistoryService.getOrderHistoryByOrderId(orderId);
        return ResponseEntity.ok(history);
    }


}
