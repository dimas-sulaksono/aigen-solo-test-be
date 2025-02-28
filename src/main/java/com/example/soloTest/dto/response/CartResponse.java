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
        this.product.setName(name);
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
}

@Data
class  UserData {
    private UUID id;
    private String name;
    private String email;
    private String role;
}