package com.example.soloTest.service;

import com.example.soloTest.dto.response.OrderHistoryResponse;
import com.example.soloTest.model.Order;
import com.example.soloTest.model.OrderHistory;
import com.example.soloTest.repository.OrderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    // get all
    public Page<OrderHistoryResponse> getAllOrderHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderHistory> historyPage = orderHistoryRepository.findAll(pageable);
        return historyPage.map(this::convertToResponse);
    }

    // save
    public void saveOrderHistory(Order order, UUID changedBy) {
        OrderHistory history = new OrderHistory();
        history.setOrderId(order.getId());
        history.setStatus(order.getStatus());
        history.setChangedBy(changedBy); // User yang mengubah status
        orderHistoryRepository.save(history);
    }

    // get by order id
    public Page<OrderHistoryResponse> getOrderHistoryByOrderId(UUID orderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderHistory> historyPage = orderHistoryRepository.findAllByOrderId(orderId, pageable);
        return historyPage.map(this::convertToResponse);
    }


    // convert to response
    private OrderHistoryResponse convertToResponse(OrderHistory history) {
        OrderHistoryResponse response = new OrderHistoryResponse();
        response.setId(history.getId());
        response.setOrderId(history.getOrderId());
        response.setStatus(history.getStatus());
        response.setChangedAt(history.getChangedAt());
        response.setChangedBy(history.getChangedBy());
        return response;
    }
}
