package com.example.soloTest.controller;

import com.example.soloTest.dto.response.ApiResponse;
import com.example.soloTest.dto.response.OrderResponse;
import com.example.soloTest.dto.response.PaginatedResponse;
import com.example.soloTest.exception.DataNotFoundException;
import com.example.soloTest.service.CheckoutService;
import com.example.soloTest.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CheckoutService checkoutService;

    // checkout
    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> checkout(@PathVariable UUID userId) {
        OrderResponse orderResponse = checkoutService.checkout(userId);
        return ResponseEntity.ok(orderResponse);
    }

    // get all order
    @GetMapping
    public ResponseEntity<?> getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<OrderResponse> orders = orderService.findAll(page, size);
            return ResponseEntity.ok(new PaginatedResponse<>(200, orders));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve orders: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable UUID id) {
        try {
            OrderResponse order = orderService.findByUserId(id)
                    .orElseThrow(() -> new RuntimeException("Order with id " + id + " not found"));
            return ResponseEntity.ok(new ApiResponse<>(200, order));
        } catch (DataNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to find product: " + e.getMessage()));
        }
    }

}
