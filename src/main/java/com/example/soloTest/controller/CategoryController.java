package com.example.soloTest.controller;

import com.example.soloTest.dto.request.CategoryRequest;
import com.example.soloTest.dto.response.ApiResponse;
import com.example.soloTest.dto.response.CategoryResponse;
import com.example.soloTest.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        try {
            CategoryResponse response = categoryService.getCategoryByName(name).get();
            return ResponseEntity
                    .status((HttpStatus.OK).value())
                    .body(new ApiResponse<>(HttpStatus.OK.value(), response));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            CategoryResponse response = categoryService.getCategoryById(id);
            return ResponseEntity
                    .status((HttpStatus.OK).value())
                    .body(new ApiResponse<>(HttpStatus.OK.value(), response));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryResponse response = categoryService.createCategory(categoryRequest);
            return ResponseEntity
                    .status((HttpStatus.OK).value())
                    .body(new ApiResponse<>(HttpStatus.OK.value(), response));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryResponse response = categoryService.updateCategory(id, categoryRequest);
            return ResponseEntity
                    .status((HttpStatus.OK).value())
                    .body(new ApiResponse<>(HttpStatus.OK.value(), response));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity
                    .status((HttpStatus.OK).value())
                    .body(new ApiResponse<>(HttpStatus.OK.value(), "Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
        }
    }
}
