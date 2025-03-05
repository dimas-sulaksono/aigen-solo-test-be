package com.example.soloTest.service;

import com.example.soloTest.dto.response.OrderResponse;
import com.example.soloTest.model.*;
import com.example.soloTest.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CheckoutService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderResponse checkout(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Ambil semua item dari cart berdasarkan user
        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        // Hitung total harga pesanan
        double totalPrice = cartItems.stream()
                .mapToDouble(cart -> cart.getProduct().getPrice() * cart.getQuantity())
                .sum();

        // Buat order baru
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Simpan histori order pertama kali dibuat
        orderHistoryService.saveOrderHistory(order, userId);

        // Copy semua item dari cart ke order_items
        for (Cart cart : cartItems) {
            Product product = cart.getProduct();

            // Update stok produk
            if (product.getStock() < cart.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - cart.getQuantity());
            productRepository.save(product);

            // Simpan order_items
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setPrice((product.getPrice() * cart.getQuantity())); // Subtotal
            orderItemRepository.save(orderItem);
        }

        // Kosongkan cart setelah checkout
        cartRepository.deleteByUser(user);

        return convertToResponse(order);
    }


    private OrderResponse convertToResponse(Order order) {
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
}
