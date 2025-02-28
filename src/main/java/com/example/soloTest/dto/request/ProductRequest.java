package com.example.soloTest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;
    private MultipartFile imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
