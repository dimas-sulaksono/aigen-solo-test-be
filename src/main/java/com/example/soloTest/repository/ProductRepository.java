package com.example.soloTest.repository;

import com.example.soloTest.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>  {

    Page<Product> findAll(Pageable pageable);

    List<Product> findByNameContainingIgnoreCase(String name);

    Optional<Product> findById(Long id);

    // find by category name
    @Query("SELECT p FROM Product p JOIN p.category c WHERE LOWER(c.name) = LOWER(:categoryName)")
    Page<Product> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

}
