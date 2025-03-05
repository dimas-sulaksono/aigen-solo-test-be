package com.example.soloTest.repository;

import com.example.soloTest.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    // find all by order id
    List<OrderHistory> findAllByOrderId(UUID orderId);
}
