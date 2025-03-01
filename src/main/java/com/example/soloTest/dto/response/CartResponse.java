package com.example.soloTest.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CartResponse {
    private Long id;
    private UserData user;
    private ProductData product;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CartResponse() {
        this.user = new UserData();
        this.product = new ProductData();
    }

    public void setUserId(UUID id) {
        this.user.setId(id);
    }

    public void setUsername(String name) {
        this.user.setName(name);
    }

    public void setEmail(String email) {
        this.user.setEmail(email);
    }

    public void setRole(String role) {
        this.user.setRole(role);
    }

    public void setProductId(Long id) {
        this.product.setId(id);
    }

    public void setProductName(String name) {
        this.product.setName(name);
    }
}

@Data
class ProductData {
    private Long id;
    private String name;
    private Double price;
}

@Data
class  UserData {
    private UUID id;
    private String name;
    private String email;
    private String role;
}