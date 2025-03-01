package com.example.soloTest.dto.response;

import com.example.soloTest.model.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemResponse {
    private Long id;
    //private OrderData order;
    private ProductData product;
    private int quantity;
    private Double price;

    public OrderItemResponse() {
        this.product = new ProductData();
    }

    public void setProductId(Long id) {
        this.product.setId(id);
    }

    public void setProductName(String name) {
        this.product.setName(name);
    }

    public void setProductPrice(Double price) {
        this.product.setPrice(price);
    }

//    public OrderItemResponse() {
//        this.order = new OrderData();
//        this.product = new ProductData();
//    }
//
//    public void setOrderId(UUID id) {
//        this.order.setId(id);
//    }
//
//    public void setUserId(UUID id) {
//        this.order.setUserId(id);
//    }
//
//    public void setStatus(OrderStatus status) {
//        this.order.setStatus(status);
//    }
//
//    public void setTotalPrice(Double price) {
//        this.order.setTotalPrice(price);
//    }
//
//    public void setProductId(Long id) {
//        this.product.setId(id);
//    }
//
//    public void setProductName(String name) {
//        this.product.setName(name);
//    }
//
//    public void setProductPrice(Double price) {
//        this.product.setPrice(price);
//    }
}

@Data
class OrderData {
    private UUID Id;
    private UUID userId;
    private OrderStatus status;
    private Double totalPrice;
}