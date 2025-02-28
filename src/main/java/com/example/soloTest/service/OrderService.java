package com.example.soloTest.service;

import com.example.soloTest.dto.response.OrderResponse;
import com.example.soloTest.model.Order;
import com.example.soloTest.repository.OrderRepository;
import com.example.soloTest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // find all
    public Page<OrderResponse> findAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders = orderRepository.findAll(pageable);
            return orders.map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find all orders: " + e.getMessage(), e);
        }
    }

    public Optional<OrderResponse> findByUserId(UUID id) {
        try {
            return orderRepository.findByUserId(id).map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find order by user id: " + e.getMessage(), e);
        }
    }

    // convert to response
    private  OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setUsername(order.getUser().getUsername());
        response.setEmail(order.getUser().getEmail());
        response.setRole(order.getUser().getRole());
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }

}
