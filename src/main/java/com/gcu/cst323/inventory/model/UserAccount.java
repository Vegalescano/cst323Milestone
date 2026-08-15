package com.gcu.cst323.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
/**
 * Entity class representing a user account.
 * User accounts store login credentials, role information, creation date, and optional linked customer data.
 */
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @NotBlank(message = "Username is required.")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Password hash is required.")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotBlank(message = "Role is required.")
    @Column(nullable = false, length = 30)
    private String role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "createdByUser")
    private List<OrderRecord> createdOrders = new ArrayList<>();

    public UserAccount() {
    }
    /**
     * Creates a user account with login credentials and a role.
     *
     * @param username username for the account
     * @param passwordHash hashed password for the account
     * @param role role assigned to the user
     */
    public UserAccount(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    /**
     * Creates a user account with login credentials and a role.
     *
     * @param username username for the account
     * @param passwordHash hashed password for the account
     * @param role role assigned to the user
     */
    public UserAccount(String username, String passwordHash, String role, Customer customer) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.customer = customer;
    }
    /**
     * Sets the account creation time before the user account is saved.
     */
    @PrePersist
    public void setDefaultCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    /**
     * Gets the user account id.
     *
     * @return user account id
     */
    public Long getUserId() {
        return userId;
    }
    /**
     * Sets the user account id.
     *
     * @param userId user account id to store
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * Sets the user account id.
     *
     * @param userId user account id to store
     */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * Sets the customer linked to this user account.
     *
     * @param customer customer to link to this account
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    /**
     * Gets the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username.
     *
     * @param username username to store
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Gets the hashed password.
     *
     * @return hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }
    /**
     * Sets the hashed password.
     *
     * @param passwordHash hashed password to store
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    /**
     * Gets the user role.
     *
     * @return user role
     */
    public String getRole() {
        return role;
    }
    /**
     * Sets the user role.
     *
     * @param role user role to store
     */
    public void setRole(String role) {
        this.role = role;
    }
    /**
     * Gets the account creation date and time.
     *
     * @return creation date and time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    /**
     * Sets the account creation date and time.
     *
     * @param createdAt creation date and time to store
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    /**
     * Gets the orders created by this user account.
     *
     * @return list of orders created by the user
     */
    public List<OrderRecord> getCreatedOrders() {
        return createdOrders;
    }
    /**
     * Sets the orders created by this user account.
     *
     * @param createdOrders list of orders created by the user
     */
    public void setCreatedOrders(List<OrderRecord> createdOrders) {
        this.createdOrders = createdOrders;
    }
}