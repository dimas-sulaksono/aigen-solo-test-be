package com.example.soloTest.repository;

import com.example.soloTest.model.OrderHistory;
import com.example.soloTest.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    Page<OrderHistory> findAllByOrderId(UUID orderId, Pageable pageable);
}
