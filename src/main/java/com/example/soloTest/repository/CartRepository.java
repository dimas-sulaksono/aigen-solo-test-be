package com.example.soloTest.repository;

import com.example.soloTest.model.Cart;
import com.example.soloTest.model.Product;
import com.example.soloTest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // find by user and product
    Optional<Cart> findByUserAndProduct (User user, Product product);

    // find by user
    List<Cart> findByUser(User user);

    // Hapus semua item di cart setelah checkout
    void deleteByUser(User user);
}
