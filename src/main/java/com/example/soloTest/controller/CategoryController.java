package com.example.soloTest.controller;

import com.example.soloTest.dto.response.ApiResponse;
import com.example.soloTest.dto.response.CategoryResponse;
import com.example.soloTest.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // get all categories
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            List<CategoryResponse> response = categoryService.getAllCategories();
            return ResponseEntity
                    .status((HttpStatus.OK).value())
                    .body(new ApiResponse<>(HttpStatus.OK.value(), response));
        } catch (Exception e) {
            List<CategoryResponse> responses = categoryService.getAllCategories();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }
}
