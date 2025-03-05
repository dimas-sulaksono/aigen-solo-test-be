package com.example.soloTest.service;

import com.example.soloTest.dto.response.OrderHistoryResponse;
import com.example.soloTest.model.Order;
import com.example.soloTest.model.OrderHistory;
import com.example.soloTest.model.User;
import com.example.soloTest.repository.OrderHistoryRepository;
import com.example.soloTest.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    // get all
    public Page<OrderHistoryResponse> getAllOrderHistory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderHistory> historyPage = orderHistoryRepository.findAll(pageable);
        return historyPage.map(this::convertToResponse);
    }

    // save
    @Transactional
    public void saveOrderHistory(Order order, UUID changedBy) {
        User user = userRepository.findById(changedBy)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OrderHistory history = new OrderHistory();
        history.setOrder(order);
        history.setStatus(order.getStatus());
        history.setChangedBy(user);
        orderHistoryRepository.save(history);
    }

    // get by order id
    public List<OrderHistoryResponse> getOrderHistoryByOrderId(UUID orderId) {
        return orderHistoryRepository.findAllByOrderId(orderId)
                .stream()
                .sorted((o1, o2) -> o2.getChangedAt().compareTo(o1.getChangedAt())) // Urutkan dari terbaru ke lama
                .map(this::convertToResponse) // Konversi ke DTO
                .collect(Collectors.toList());
    }


    // convert to response
    private OrderHistoryResponse convertToResponse(OrderHistory history) {
        OrderHistoryResponse response = new OrderHistoryResponse();
        response.setId(history.getId());
        response.setOrderId(history.getOrder().getId());
        response.setStatus(history.getStatus());
        response.setChangedAt(history.getChangedAt());
        response.setChangedBy(history.getChangedBy().getId());
        return response;
    }
}
