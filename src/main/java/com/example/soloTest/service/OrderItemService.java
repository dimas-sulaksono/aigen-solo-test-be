package com.example.soloTest.service;

import com.example.soloTest.dto.response.OrderItemResponse;
import com.example.soloTest.model.OrderItem;
import com.example.soloTest.repository.OrderItemRepository;
import com.example.soloTest.repository.OrderRepository;
import com.example.soloTest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    // find all
    public Page<OrderItemResponse> findAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<OrderItem> orderItems = orderItemRepository.findAll(pageable);
            return orderItems.map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find all order items: " + e.getMessage(), e);
        }
    }

    private OrderItemResponse convertToResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();

        response.setId(orderItem.getId());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());

        //response.setOrderId(orderItem.getOrder().getId());
        //response.setStatus(orderItem.getOrder().getStatus());
        //response.setTotalPrice(orderItem.getOrder().getTotalPrice());

        //response.setUserId(orderItem.getOrder().getUser().getId());

        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setProductPrice(orderItem.getProduct().getPrice());

        return response;
    }
}
