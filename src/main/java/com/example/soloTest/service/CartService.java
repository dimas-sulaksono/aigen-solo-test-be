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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    //create cart
    @Transactional
    public CartResponse createCart(CartRequest cartRequest) {
        try {
            if (cartRequest.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }

            User user = userRepository.findById(cartRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productRepository.findById(cartRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < cartRequest.getQuantity()) {
                throw new RuntimeException("Stock not sufficient");
            }

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(cartRequest.getQuantity());

            Cart savedCart = cartRepository.save(cart);
            return convertToResponse(savedCart);

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

    // convert to response
    private CartResponse convertToResponse(Cart cart) {
        CartResponse response = new CartResponse();

        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setUsername(cart.getUser().getUsername());
        response.setProductId(cart.getProduct().getId());
        response.setProductName(cart.getProduct().getName());
        response.setQuantity(cart.getQuantity());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());

        return response;
    }
}















