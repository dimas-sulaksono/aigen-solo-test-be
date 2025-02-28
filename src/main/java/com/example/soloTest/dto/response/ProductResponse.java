package com.example.soloTest.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private CategoryData category;
    private Boolean status;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse() {
        this.category = new CategoryData();
    }

    public void setCategoryId(Long id) {
        this.category.setId(id);
    }

    public void setCategoryName(String name) {
        this.category.setName(name);
    }
}

@Data
class CategoryData {
    private Long id;
    private String name;
}