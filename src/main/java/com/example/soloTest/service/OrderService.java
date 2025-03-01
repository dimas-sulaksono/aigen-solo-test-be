package com.example.soloTest.service;

import com.example.soloTest.dto.response.OrderDetailDTO;
import com.example.soloTest.dto.response.OrderItemResponse;
import com.example.soloTest.dto.response.OrderResponse;
import com.example.soloTest.model.Order;
import com.example.soloTest.model.OrderItem;
import com.example.soloTest.model.OrderStatus;
import com.example.soloTest.repository.OrderItemRepository;
import com.example.soloTest.repository.OrderRepository;
import com.example.soloTest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    // find by user id
    public Optional<OrderResponse> findByUserId(UUID id) {
        try {
            return orderRepository.findByUserId(id).map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find order by user id: " + e.getMessage(), e);
        }
    }

    // order detail
    public Optional<OrderDetailDTO> getOrderDetail(UUID orderId) {
        return orderRepository.findById(orderId).map(order -> {
            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setOrderId(order.getId());
            dto.setUserId(order.getUser().getId());
            dto.setStatus(order.getStatus());
            dto.setTotalPrice(order.getTotalPrice());

            List<OrderItemResponse> items = orderItemRepository.findByOrderId(orderId)
                    .stream()
                    .map(this::convertToOrderItemResponse)
                    .collect(Collectors.toList());

            dto.setOrderItems(items);
            return dto;
        });
    }

    // update status
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found"));

        order.setStatus(newStatus);
        orderRepository.save(order);

        return convertToResponse(order);
    }


    // convert to response
    private  OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        //response.setUsername(order.getUser().getUsername());
        //response.setEmail(order.getUser().getEmail());
        //response.setRole(order.getUser().getRole());
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }

    private OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();

        response.setId(orderItem.getId());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getQuantity() * orderItem.getPrice()); // Perbaikan harga total per item

        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setProductPrice(orderItem.getProduct().getPrice());

        return response;
    }



}
