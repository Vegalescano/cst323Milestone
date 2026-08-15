package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for accessing Customer records through Spring Data JPA.
 * Provides built in CRUD operations and custom customer lookup queries.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	/**
	 * Searches customers by first name, last name, or email using case-insensitive matching.
	 *
	 * @param firstName first name search text
	 * @param lastName last name search text
	 * @param email email search text
	 * @return list of customers matching the search values
	 */
    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName, String lastName, String email);
    /**
     * Finds a customer by email address using case-insensitive matching.
     *
     * @param email email address to search for
     * @return optional customer if found
     */
    Optional<Customer> findByEmailIgnoreCase(String email);
}