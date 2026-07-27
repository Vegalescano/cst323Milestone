package com.gcu.cst323.inventory.service;

import com.gcu.cst323.inventory.model.Customer;
import com.gcu.cst323.inventory.model.OrderItem;
import com.gcu.cst323.inventory.model.OrderRecord;
import com.gcu.cst323.inventory.model.Product;
import com.gcu.cst323.inventory.model.UserAccount;
import com.gcu.cst323.inventory.repository.CustomerRepository;
import com.gcu.cst323.inventory.repository.OrderRecordRepository;
import com.gcu.cst323.inventory.repository.ProductRepository;
import com.gcu.cst323.inventory.repository.UserAccountRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRecordRepository orderRecordRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final UserAccountRepository userAccountRepository;

    public OrderService(OrderRecordRepository orderRecordRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        UserAccountRepository userAccountRepository) {
        this.orderRecordRepository = orderRecordRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public List<OrderRecord> findAll() {
        return orderRecordRepository.findAll().stream()
                .sorted(Comparator.comparing(OrderRecord::getOrderDate).reversed())
                .toList();
    }

    public OrderRecord findById(Long id) {
        return orderRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    @Transactional
    public OrderRecord createOrder(Long customerId, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalArgumentException("Cannot order an inactive product.");
        }

        if (product.getQuantityInStock() < quantity) {
            throw new IllegalArgumentException("Quantity requested exceeds current stock.");
        }

        UserAccount user = userAccountRepository.findByUsername("admin")
                .orElseGet(() -> userAccountRepository.save(
                        new UserAccount("admin", "demo-password", "ADMIN")
                ));

        product.setQuantityInStock(product.getQuantityInStock() - quantity);
        productRepository.save(product);

        OrderRecord order = new OrderRecord(customer, user, LocalDate.now(), "Pending");
        order.addOrderItem(new OrderItem(product, quantity));

        return orderRecordRepository.save(order);
    }

    public OrderRecord updateStatus(Long orderId, String status) {
        OrderRecord order = findById(orderId);
        order.setStatus(status);
        return orderRecordRepository.save(order);
    }

    public void delete(Long id) {
        orderRecordRepository.deleteById(id);
    }

    public long countOpenOrders() {
        return orderRecordRepository.countByStatusIgnoreCase("Pending");
    }
}