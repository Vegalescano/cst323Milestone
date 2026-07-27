package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName, String lastName, String email);

    Optional<Customer> findByEmailIgnoreCase(String email);
}