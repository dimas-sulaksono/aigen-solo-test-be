package com.example.soloTest.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private UserData user;
    private String status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderResponse() {
        this.user = new UserData();
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
}