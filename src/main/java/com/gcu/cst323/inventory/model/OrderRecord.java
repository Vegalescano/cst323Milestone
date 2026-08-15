package com.gcu.cst323.inventory.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_record")
/**
 * Entity class representing a customer order.
 * An order belongs to a customer, is created by a user account, and contains order items.
 */
public class OrderRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private UserAccount createdByUser;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false, length = 40)
    private String status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "orderRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

    public OrderRecord() {
    }
    /**
     * Creates an order record with the selected customer, creator, date, and status.
     *
     * @param customer customer placing the order
     * @param createdByUser user account creating the order
     * @param orderDate date the order is created
     * @param status initial order status
     */
    public OrderRecord(Customer customer, UserAccount createdByUser, LocalDate orderDate, String status) {
        this.customer = customer;
        this.createdByUser = createdByUser;
        this.orderDate = orderDate;
        this.status = status;
    }
    /**
     * Adds an order item to this order and links the item back to the order record.
     *
     * @param item order item to add
     */
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrderRecord(this);
        recalculateTotal();
    }
    /**
     * Recalculates the total amount by adding the line totals for all order items.
     */
    @PrePersist
    @PreUpdate
    public void recalculateTotal() {
        totalAmount = orderItems.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    /**
     * Gets the order id.
     *
     * @return order id
     */
    public Long getOrderId() {
        return orderId;
    }
    /**
     * Sets the order id.
     *
     * @param orderId order id to store
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    /**
     * Gets the customer associated with the order.
     *
     * @return customer who placed the order
     */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * Sets the customer associated with the order.
     *
     * @param customer customer to associate with the order
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    /**
     * Sets the user account that created the order.
     *
     * @param createdByUser user account to store as the order creator
     */
    public UserAccount getCreatedByUser() {
        return createdByUser;
    }
    /**
     * Sets the customer associated with the order.
     *
     * @param customer customer to associate with the order
     */
    public void setCreatedByUser(UserAccount createdByUser) {
        this.createdByUser = createdByUser;
    }
    /**
     * Sets the user account that created the order.
     *
     * @param createdByUser user account to store as the order creator
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }
    /**
/**
 * Sets the order date.
 *
 * @param orderDate order date to store
 */
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    /**
     * Sets the order date.
     *
     * @param orderDate order date to store
     */
    public String getStatus() {
        return status;
    }
    /**
     * Gets the order status.
     *
     * @return order status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * Gets the total order amount.
     *
     * @return total order amount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    /**
     * Sets the total order amount.
     *
     * @param totalAmount total order amount to store
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    /**
     * Gets the order items contained in this order.
     *
     * @return list of order items
     */
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    /**
     * Sets the order items contained in this order.
     *
     * @param orderItems list of order items to associate with the order
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}