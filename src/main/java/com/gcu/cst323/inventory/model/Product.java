package com.gcu.cst323.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
/**
 * Gets the total order amount.
 *
 * @return total order amount
 */
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @NotBlank(message = "Product name is required.")
    @Column(name = "product_name", nullable = false, length = 120)
    private String productName;

    @Column(length = 255)
    private String description;

    @DecimalMin(value = "0.00", message = "Price cannot be negative.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Min(value = 0, message = "Quantity cannot be negative.")
    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @Min(value = 0, message = "Reorder level cannot be negative.")
    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Product() {
    }
    /**
     * Creates a product with inventory and pricing information.
     *
     * @param productName product name
     * @param description product description
     * @param price product price
     * @param quantityInStock quantity currently in stock
     * @param reorderLevel reorder level used to identify low stock
     * @param active active status for the product
     */
    public Product(String productName, String description, BigDecimal price,
                   Integer quantityInStock, Integer reorderLevel, Boolean active) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.reorderLevel = reorderLevel;
        this.active = active;
    }
    /**
     * Creates a product with inventory and pricing information.
     *
     * @param productName product name
     * @param description product description
     * @param price product price
     * @param quantityInStock quantity currently in stock
     * @param reorderLevel reorder level used to identify low stock
     * @param active active status for the product
     * 
     *  * Determines whether the product quantity is at or below the reorder level.
     *
     * @return true when the product is low stock, otherwise false
     */
    public boolean isLowStock() {
        if (quantityInStock == null || reorderLevel == null) {
            return false;
        }
        return quantityInStock <= reorderLevel;
    }
    /**
     * Gets the product id.
     *
     * @return product id
     */
    public Long getProductId() {
        return productId;
    }
    /**
     * Sets the product id.
     *
     * @param productId product id to store
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    /**
     * Sets the product id.
     *
     * @param productId product id to store
     */
    public String getProductName() {
        return productName;
    }
    /**
     * Gets the product name.
     *
     * @return product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    /**
     * Sets the product name.
     *
     * @param productName product name to store
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the product description.
     *
     * @return product description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Sets the product description.
     *
     * @param description product description to store
     */
    public BigDecimal getPrice() {
        return price;
    }
    /**
     * Gets the product price.
     *
     * @return product price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    /**
     * Sets the product price.
     *
     * @param price product price to store
     */
    public Integer getQuantityInStock() {
        return quantityInStock;
    }
    /**
     * Gets the quantity currently in stock.
     *
     * @return quantity in stock
     */
    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    /**
     * Gets the reorder level used for low-stock checks.
     *
     * @return reorder level
     */
    public Integer getReorderLevel() {
        return reorderLevel;
    }
    /**
     * Gets the reorder level used for low-stock checks.
     *
     * @return reorder level
     */
    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    /**
     * Sets the reorder level used for low-stock checks.
     *
     * @param reorderLevel reorder level to store
     */
    public Boolean getActive() {
        return active;
    }
    /**
     * Sets the reorder level used for low-stock checks.
     *
     * @param reorderLevel reorder level to store
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
    /**
     * Gets the order items that reference this product.
     *
     * @return list of order items using this product
     */
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    /**
     * Gets the order items that reference this product.
     *
     * @return list of order items using this product
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}