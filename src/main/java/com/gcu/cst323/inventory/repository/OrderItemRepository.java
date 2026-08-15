package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for accessing OrderItem records through Spring Data JPA.
 * Provides built in CRUD operations for order line items.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}