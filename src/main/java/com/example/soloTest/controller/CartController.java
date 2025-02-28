package com.example.soloTest.controller;

import com.example.soloTest.dto.request.CartRequest;
import com.example.soloTest.dto.response.ApiResponse;
import com.example.soloTest.dto.response.CartResponse;
import com.example.soloTest.dto.response.CategoryResponse;
import com.example.soloTest.dto.response.PaginatedResponse;
import com.example.soloTest.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // add to cart
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartRequest cartRequest) {
        try {
            CartResponse response = cartService.addToCart(cartRequest);
            return  ResponseEntity.ok(new ApiResponse<>(200, response));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    // get all cart
    @GetMapping
    public ResponseEntity<?> getAllCart(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CartResponse> carts = cartService.findAll(page, size);
            return ResponseEntity.ok(new PaginatedResponse<>(200, carts));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve carts: " + e.getMessage()));
        }
    }

    // get cart by user id
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable UUID userId) {
        List<CartResponse> carts = cartService.findByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, carts));

    }
}
