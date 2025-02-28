package com.example.soloTest.controller;

import com.example.soloTest.dto.request.CartRequest;
import com.example.soloTest.dto.response.ApiResponse;
import com.example.soloTest.dto.response.CartResponse;
import com.example.soloTest.dto.response.PaginatedResponse;
import com.example.soloTest.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CartRequest cartRequest) {
        try {
            CartResponse response = cartService.createCart(cartRequest);
            return  ResponseEntity.ok(new ApiResponse<>(200, response));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

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
}
