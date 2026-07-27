package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String productName, String description);

    long countByActiveTrue();
}