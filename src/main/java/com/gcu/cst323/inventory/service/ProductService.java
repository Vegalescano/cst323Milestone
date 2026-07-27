package com.gcu.cst323.inventory.service;

import com.gcu.cst323.inventory.model.Product;
import com.gcu.cst323.inventory.repository.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(String search) {
        List<Product> products;

        if (search != null && !search.isBlank()) {
            String query = search.trim();
            products = productRepository
                    .findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .filter(product -> Boolean.TRUE.equals(product.getActive()))
                .sorted(Comparator.comparing(Product::getProductName))
                .toList();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    public Product save(Product product) {
        if (product.getActive() == null) {
            product.setActive(true);
        }
        return productRepository.save(product);
    }

    public void deactivate(Long id) {
        Product product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    public long countActiveProducts() {
        return productRepository.countByActiveTrue();
    }

    public List<Product> findLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> Boolean.TRUE.equals(product.getActive()))
                .filter(Product::isLowStock)
                .sorted(Comparator.comparing(Product::getProductName))
                .toList();
    }
}