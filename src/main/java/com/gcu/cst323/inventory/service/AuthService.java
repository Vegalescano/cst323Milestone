package com.gcu.cst323.inventory.service;

import com.gcu.cst323.inventory.model.Customer;
import com.gcu.cst323.inventory.model.UserAccount;
import com.gcu.cst323.inventory.repository.CustomerRepository;
import com.gcu.cst323.inventory.repository.UserAccountRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserAccountRepository userAccountRepository;
    private final CustomerRepository customerRepository;

    public AuthService(UserAccountRepository userAccountRepository,
                       CustomerRepository customerRepository) {
        this.userAccountRepository = userAccountRepository;
        this.customerRepository = customerRepository;
    }

    public UserAccount register(String username, String password, String role) {
        return register(username, password, role, null, null, null, null, null);
    }

    public UserAccount register(String username,
                                String password,
                                String role,
                                String customerFirstName,
                                String customerLastName,
                                String customerEmail,
                                String customerPhone,
                                String customerAddress) {
        String cleanUsername = username == null ? "" : username.trim();
        String cleanPassword = password == null ? "" : password.trim();

        if (cleanUsername.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (cleanPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (userAccountRepository.findByUsername(cleanUsername).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        String selectedRole = role == null || role.isBlank() ? "CUSTOMER" : role.trim().toUpperCase();
        Customer linkedCustomer = null;

        if ("CUSTOMER".equals(selectedRole)) {
            String cleanFirstName = customerFirstName == null ? "" : customerFirstName.trim();
            String cleanLastName = customerLastName == null ? "" : customerLastName.trim();
            String cleanEmail = customerEmail == null ? "" : customerEmail.trim();
            String cleanPhone = customerPhone == null ? "" : customerPhone.trim();
            String cleanAddress = customerAddress == null ? "" : customerAddress.trim();

            if (cleanFirstName.isBlank()) {
                throw new IllegalArgumentException("Customer first name is required.");
            }

            if (cleanLastName.isBlank()) {
                throw new IllegalArgumentException("Customer last name is required.");
            }

            if (cleanEmail.isBlank()) {
                throw new IllegalArgumentException("Customer email is required.");
            }

            Optional<Customer> existingCustomer = customerRepository.findByEmailIgnoreCase(cleanEmail);

            if (existingCustomer.isPresent()) {
                linkedCustomer = existingCustomer.get();
            } else {
                linkedCustomer = customerRepository.save(
                        new Customer(cleanFirstName, cleanLastName, cleanEmail, cleanPhone, cleanAddress)
                );
            }
        }

        UserAccount user = new UserAccount(
                cleanUsername,
                hashPassword(cleanPassword),
                selectedRole,
                linkedCustomer
        );

        return userAccountRepository.save(user);
    }

    public Optional<UserAccount> authenticate(String username, String password) {
        String cleanUsername = username == null ? "" : username.trim();
        String cleanPassword = password == null ? "" : password.trim();

        if (cleanUsername.isBlank() || cleanPassword.isBlank()) {
            return Optional.empty();
        }

        Optional<UserAccount> userResult = userAccountRepository.findByUsername(cleanUsername);

        if (userResult.isEmpty()) {
            return Optional.empty();
        }

        UserAccount user = userResult.get();
        String storedPassword = user.getPasswordHash();
        String hashedInput = hashPassword(cleanPassword);

        if (hashedInput.equals(storedPassword)) {
            return Optional.of(user);
        }

        if (cleanPassword.equals(storedPassword)) {
            user.setPasswordHash(hashedInput);
            userAccountRepository.save(user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    public Optional<UserAccount> findByUsername(String username) {
        String cleanUsername = username == null ? "" : username.trim();

        if (cleanUsername.isBlank()) {
            return Optional.empty();
        }

        return userAccountRepository.findByUsername(cleanUsername);
    }

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(encodedHash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Password hashing failed.", exception);
        }
    }
}