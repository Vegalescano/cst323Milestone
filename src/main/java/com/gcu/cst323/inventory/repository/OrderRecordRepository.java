package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRecordRepository extends JpaRepository<OrderRecord, Long> {
    long countByStatusIgnoreCase(String status);
    
    boolean existsByCustomer_CustomerId(Long customerId);
}