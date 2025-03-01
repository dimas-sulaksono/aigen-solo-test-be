package com.example.soloTest.service;

import com.example.soloTest.dto.request.CartRequest;
import com.example.soloTest.dto.request.CategoryRequest;
import com.example.soloTest.dto.request.ProductRequest;
import com.example.soloTest.dto.response.CartResponse;
import com.example.soloTest.exception.DataNotFoundException;
import com.example.soloTest.model.Cart;
import com.example.soloTest.model.Product;
import com.example.soloTest.model.User;
import com.example.soloTest.repository.CartRepository;
import com.example.soloTest.repository.ProductRepository;
import com.example.soloTest.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // add to cart
    @Transactional
    public CartResponse addToCart(CartRequest cartRequest) {
        try {
            User user = userRepository.findById(cartRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Product product = productRepository.findById(cartRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < cartRequest.getQuantity()) {
                throw new RuntimeException("Stock not sufficient");
            }

            Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);
            if (existingCart.isPresent()) {
                Cart cart = existingCart.get();
                cart.setQuantity(cartRequest.getQuantity());
                Cart savedCart = cartRepository.save(cart);
                return convertToResponse(savedCart);
            } else {
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setProduct(product);
                cart.setQuantity(cartRequest.getQuantity());
                Cart savedCart = cartRepository.save(cart);
                return convertToResponse(savedCart);
            }
        } catch (DataNotFoundException e) {
            throw e;
        }
    }

    // find all carts
    public Page<CartResponse> findAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<Cart> carts = cartRepository.findAll(pageable);
            return carts.map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find all carts: " + e.getMessage(), e);
        }
    }

    // find by user
    public List<CartResponse> findByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Cart> cartList = cartRepository.findByUser(user);

        return cartList
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // convert to response
    private CartResponse convertToResponse(Cart cart) {
        CartResponse response = new CartResponse();

        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setUsername(cart.getUser().getUsername());
        response.setEmail(cart.getUser().getEmail());
        response.setRole(cart.getUser().getRole());
        response.setProductId(cart.getProduct().getId());
        response.setProductName(cart.getProduct().getName());
        response.setQuantity(cart.getQuantity());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());

        return response;
    }
}















