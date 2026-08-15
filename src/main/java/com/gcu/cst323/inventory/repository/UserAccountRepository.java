package com.gcu.cst323.inventory.repository;

import com.gcu.cst323.inventory.model.UserAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for accessing UserAccount records through Spring Data JPA.
 * Provides built-in CRUD operations and username lookup for authentication.
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	/**
	 * Finds a user account by username.
	 *
	 * @param username username to search for
	 * @return optional user account if found
	 */
    Optional<UserAccount> findByUsername(String username);
}