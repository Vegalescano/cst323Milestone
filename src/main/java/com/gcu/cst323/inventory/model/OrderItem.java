package com.gcu.cst323.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
/**
 * Entity class representing an order line item.
 * Each order item links a product to an order and stores quantity and price details.
 */
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderRecord orderRecord;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

    public OrderItem() {
    }
    /**
     * Creates an order item for a product and quantity.
     * The unit price is copied from the selected product and the line total is calculated.
     *
     * @param product product selected for the order item
     * @param quantity quantity ordered
     */
    public OrderItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    /**
     * Gets the order item id.
     *
     * @return order item id
     */
    public Long getOrderItemId() {
        return orderItemId;
    }
    /**
     * Gets the order item id.
     *
     * @return order item id
     */
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }
    /**
     * Gets the order record that owns this item.
     *
     * @return owning order record
     */
    public OrderRecord getOrderRecord() {
        return orderRecord;
    }
    /**
     * Sets the order record that owns this item.
     *
     * @param orderRecord order record to associate with this item
     */
    public void setOrderRecord(OrderRecord orderRecord) {
        this.orderRecord = orderRecord;
    }
    /**
     * Sets the order record that owns this item.
     *
     * @param orderRecord order record to associate with this item
     */
    public Product getProduct() {
        return product;
    }
    /**
     * Sets the product for this order item.
     *
     * @param product product to assign to this item
     */
    public void setProduct(Product product) {
        this.product = product;
    }
    /**
     * Sets the product for this order item.
     *
     * @param product product to assign to this item
     */
    public Integer getQuantity() {
        return quantity;
    }
    /**
     * Sets the product for this order item.
     *
     * @param product product to assign to this item
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    /**
     * Sets the ordered quantity.
     *
     * @param quantity ordered quantity to store
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    /**
     * Sets the unit price used for this order item.
     *
     * @param unitPrice unit price to store
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    /**
     * Sets the unit price used for this order item.
     *
     * @param unitPrice unit price to store
     */
    public BigDecimal getLineTotal() {
        return lineTotal;
    }
    /**
     * Gets the calculated line total.
     *
     * @return line total
     */
    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}