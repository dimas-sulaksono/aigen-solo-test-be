package com.example.soloTest.service;

import com.example.soloTest.dto.request.ProductRequest;
import com.example.soloTest.dto.response.ProductResponse;
import com.example.soloTest.exception.DataNotFoundException;
import com.example.soloTest.model.Category;
import com.example.soloTest.model.Product;
import com.example.soloTest.repository.CategoryRepository;
import com.example.soloTest.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${file.IMAGE_DIR}")
    private String imageDirectory;

    private static final long maxFileSize = 5 * 1024 * 1024; // 5MB

    private final String[] allowedImageTypes = {"image/jpeg", "image/png", "image/jpg"};

    // fungsi buat simpan gambar
    public String saveImageFile(MultipartFile file, String name) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or not provided");
        }

        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("File size exceeds the limit of " + maxFileSize + " bytes");
        }

        String fileType = file.getContentType();
        boolean isValidType = false;
        for (String allowedType : allowedImageTypes) {
            assert fileType != null;
            if (fileType.equals(allowedType)) {
                isValidType = true;
                break;
            }
        }

        if (!isValidType) {
            throw new RuntimeException("Invalid file type. Allowed types: " + String.join(", ", allowedImageTypes));
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String customFileName = name + "_" + timeStamp + fileExtension;
        //String customFileName = name + "_" + timeStamp + "_" + originalFileName;

        Path path = Path.of(imageDirectory, customFileName);
        Files.copy(file.getInputStream(), path);

        return customFileName;
    }

    // fungsi slug
    public static String toSlug(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .trim();
    }

    // create product
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        try {
            Product product = new Product();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());

            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);

            if (productRequest.getImagePath() != null && !productRequest.getImagePath().isEmpty()) {
                String imagePath = saveImageFile(productRequest.getImagePath(), toSlug(productRequest.getName()));
                product.setImagePath(imagePath);
            }

            Product savedProduct = productRepository.save(product);
            return convertToResponse(savedProduct);
        } catch (DataNotFoundException e) {
          throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product: " + e.getMessage(), e);
        }
    }

    // find all
    public Page<ProductResponse> findAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> product = productRepository.findAll(pageable);
            return product.map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find all products: " + e.getMessage(), e);
        }
    }

    // find by name
    public List<ProductResponse> findByName(String name){
        try {
            return productRepository.findByNameContainingIgnoreCase(name)
                    .stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to find product by name: " + e.getMessage(), e);
        }
    }

    //find by id
    public Optional<ProductResponse> findById(Long id){
        try {
            return productRepository.findById(id).map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find product by id: " + e.getMessage(), e);
        }
    }

    // update product
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(()-> new DataNotFoundException("Product with ID " + id + " not found"));
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());

            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(()-> new RuntimeException("Product with ID " + id + "not found"));
            product.setCategory(category);

            if (productRequest.getImagePath() != null && !productRequest.getImagePath().isEmpty()) {
                String imagePath = saveImageFile(productRequest.getImagePath(), toSlug(productRequest.getName()));
                product.setImagePath(imagePath);
            }

            Product updateProduct = productRepository.save(product);
            return convertToResponse(updateProduct);
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Failed to update product: "+ e.getMessage(), e);
        }
    }

    // delete product
    @Transactional
    public void deleteProduct(Long id) {
        try {
            if(!productRepository.existsById(id)){
                throw new DataNotFoundException("Produk with id " + id + " not found");
            }
            productRepository.deleteById(id);
        } catch (DataNotFoundException e) {
            throw  e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product: " + e.getMessage(), e);
        }
    }

    // convert to response
    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setStatus(product.getStatus());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        response.setImagePath(product.getImagePath());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }
}