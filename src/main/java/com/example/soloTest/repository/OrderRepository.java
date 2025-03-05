package com.example.soloTest.repository;

import com.example.soloTest.model.Order;
import com.example.soloTest.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    // find by user id
    Page<Order> findByUserId(UUID id, Pageable pageable);

    // find all by status
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

}
