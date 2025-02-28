package com.example.soloTest.controller;

import com.example.soloTest.dto.request.ProductRequest;
import com.example.soloTest.dto.response.ApiResponse;
import com.example.soloTest.dto.response.PaginatedResponse;
import com.example.soloTest.dto.response.ProductResponse;
import com.example.soloTest.exception.DataNotFoundException;
import com.example.soloTest.exception.DuplicateDataException;
import com.example.soloTest.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${file.IMAGE_DIR}")
    private String imageDirectory;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid @RequestBody ProductRequest productRequest) {
        try {
            ProductResponse productResponse = productService.createProduct(productRequest);
            return ResponseEntity
                    .ok(new ApiResponse<>(200, productResponse));
        } catch (DuplicateDataException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ProductResponse> products = productService.findAll(page, size);
            return ResponseEntity
                    .ok(new PaginatedResponse<>(200, products));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve products: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam String name){
        try {
            List<ProductResponse> products = productService.findByName(name);
            return ResponseEntity.ok(new ApiResponse<>(200, products));
        } catch (DataNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to find product " + e.getMessage()));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id){
        try {
            ProductResponse productResponse = productService.findByIdAndStatusTrue(id)
                    .orElseThrow(() -> new RuntimeException("Product with id " + id + " not found"));
            return ResponseEntity
                    .ok(new ApiResponse<>(200, productResponse));
        } catch (DataNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to find product " + e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProduct(@Valid @PathVariable("id") Long id, @ModelAttribute @RequestBody ProductRequest productRequest){
        try {
            ProductResponse productResponse = productService.updateProduct(id, productRequest);
            return ResponseEntity
                    .ok(new ApiResponse<>(200, productResponse));
        } catch (DataNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity
                    .ok(new ApiResponse<>(HttpStatus.OK.value(),"Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete product: " + e.getMessage()));
        }
    }


}
















