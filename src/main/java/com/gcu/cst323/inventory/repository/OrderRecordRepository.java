package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for accessing OrderRecord records through Spring Data JPA.
 * Provides built in CRUD operations and order summary queries.
 */
public interface OrderRecordRepository extends JpaRepository<OrderRecord, Long> {
	/**
	 * Counts order records by status using case insensitive matching.
	 *
	 * @param status order status to count
	 * @return number of orders with the selected status
	 */
    long countByStatusIgnoreCase(String status);
    /**
     * Checks whether order records exist for a selected customer.
     *
     * @param customerId customer id to check
     * @return true when the customer has related orders, otherwise false
     */
    boolean existsByCustomer_CustomerId(Long customerId);
}