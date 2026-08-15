package com.gcu.cst323.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
/**
 * Entity class representing a customer record.
 * Customers can be linked to user accounts and can place orders.
 */
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "First name is required.")
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Email(message = "Enter a valid email address.")
    @NotBlank(message = "Email is required.")
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(length = 255)
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<OrderRecord> orders = new ArrayList<>();

    @OneToOne(mappedBy = "customer")
    private UserAccount userAccount;

    public Customer() {
    }
    /**
     * Creates a customer with contact information.
     *
     * @param firstName customer first name
     * @param lastName customer last name
     * @param email customer email address
     * @param phone customer phone number
     * @param address customer address
     */
    public Customer(String firstName, String lastName, String email, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    /**
     * Gets the customer id.
     *
     * @return customer id
     */
    public Long getCustomerId() {
        return customerId;
    }
    /**
     * Sets the customer id.
     *
     * @param customerId customer id to store
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    /**
     * Gets the customer first name.
     *
     * @return customer first name
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Sets the customer first name.
     *
     * @param firstName customer first name to store
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Gets the customer last name.
     *
     * @return customer last name
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Sets the customer last name.
     *
     * @param lastName customer last name to store
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Gets the customer email address.
     *
     * @return customer email address
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the customer email address.
     *
     * @param email customer email address to store
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the customer phone number.
     *
     * @return customer phone number
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Sets the customer phone number.
     *
     * @param phone customer phone number to store
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * Gets the customer address.
     *
     * @return customer address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Gets the customer address.
     *
     * @return customer address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * Gets the customer address.
     *
     * @return customer address
     */
    public List<OrderRecord> getOrders() {
        return orders;
    }
    /**
     * Sets the orders placed by this customer.
     *
     * @param orders list of order records to associate with the customer
     */
    public void setOrders(List<OrderRecord> orders) {
        this.orders = orders;
    }
    /**
     * Sets the orders placed by this customer.
     *
     * @param orders list of order records to associate with the customer
     */
    public UserAccount getUserAccount() {
        return userAccount;
    }
    /**
     * Sets the user account linked to this customer.
     *
     * @param userAccount user account to link to the customer
     */
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    /**
     * Builds the customer's full display name.
     *
     * @return first name and last name combined
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}