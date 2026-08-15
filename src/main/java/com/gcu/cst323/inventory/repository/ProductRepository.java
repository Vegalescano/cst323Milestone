package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for accessing Product records through Spring Data JPA.
 * Provides built in CRUD operations and custom product search/count queries.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
	/**
	 * Searches products by product name or description using case-insensitive matching.
	 *
	 * @param productName product name search text
	 * @param description product description search text
	 * @return list of products matching the search values
	 */
    List<Product> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String productName, String description);
    /**
     * Searches products by product name or description using case-insensitive matching.
     *
     * @param productName product name search text
     * @param description product description search text
     * @return list of products matching the search values
     */
    long countByActiveTrue();
}