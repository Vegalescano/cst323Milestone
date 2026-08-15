package com.gcu.cst323.inventory.service;

import com.gcu.cst323.inventory.model.Product;
import com.gcu.cst323.inventory.repository.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
/**
 * Business service responsible for product and inventory operations.
 * Handles product search, lookup, saving, deactivation, active product counts, and low-stock results.
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;
    /**
     * Creates a ProductService with the required product repository.
     *
     * @param productRepository repository used to access product records
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    /**
     * Finds all products or filters products by search text.
     *
     * @param search optional product name or description search text
     * @return list of matching products
     */
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
    /**
     * Finds a product by id.
     *
     * @param id product id to search for
     * @return matching product
     * @throws IllegalArgumentException if the product is not found
     */
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }
    /**
     * Saves a new or edited product record.
     *
     * @param product product record to save
     * @return saved product
     */
    public Product save(Product product) {
        if (product.getActive() == null) {
            product.setActive(true);
        }
        return productRepository.save(product);
    }
    /**
     * Marks a product as inactive instead of permanently deleting it.
     *
     * @param id product id to deactivate
     */
    public void deactivate(Long id) {
        Product product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }
    /**
     * Counts products marked as active.
     *
     * @return number of active products
     */
    public long countActiveProducts() {
        return productRepository.countByActiveTrue();
    }
    /**
     * Counts products marked as active.
     *
     * @return number of active products
     */
    public List<Product> findLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> Boolean.TRUE.equals(product.getActive()))
                .filter(Product::isLowStock)
                .sorted(Comparator.comparing(Product::getProductName))
                .toList();
    }
}