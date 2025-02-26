package com.example.soloTest.service;

import com.example.soloTest.dto.request.CategoryRequest;
import com.example.soloTest.dto.response.CategoryResponse;
import com.example.soloTest.exception.DataNotFoundException;
import com.example.soloTest.exception.DuplicateDataException;
import com.example.soloTest.model.Category;
import com.example.soloTest.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // get all categories
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList(); }

    // get category by name
    public Optional<CategoryResponse> getCategoryByName(String name) {
        try {
            return categoryRepository.findByName(name)
                    .map(this::convertToResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find category by name: " + e.getMessage(), e);
        }
    }

    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }

    // create category
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        try {
            if (categoryRepository.findByName((categoryRequest.getName())).isPresent()) {
                throw new RuntimeException("Category with name " + categoryRequest.getName() + " already exists");
            }
            Category category = new Category();
            category.setName(categoryRequest.getName());
            category = categoryRepository.save(category);
            return convertToResponse(category);
        } catch (DuplicateDataException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category: " + e.getMessage(), e);
        }

    }

    // update category
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Category with id " + id + " not found"));
            if (categoryRepository.findByName(categoryRequest.getName()).isPresent()) {
                throw new RuntimeException("Category with name " + categoryRequest.getName() + " already exists");
            } else {
            category.setName(categoryRequest.getName());
            category = categoryRepository.save(category);
            return convertToResponse(category);
            }
        } catch (DataNotFoundException e) {
            throw e;
        } catch (DuplicateDataException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update category: " + e.getMessage(), e);
        }
    }

    // delete category
    public void deleteCategory(Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                throw new DataNotFoundException("Category with id " + id + " not found");
            }
            categoryRepository.deleteById(id);
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete category: " + e.getMessage(), e);
        }
    }

    // convert to response
    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}
