package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}