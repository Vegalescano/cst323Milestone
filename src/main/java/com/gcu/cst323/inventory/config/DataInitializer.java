package com.gcu.cst323.inventory.config;

import com.gcu.cst323.inventory.model.Customer;
import com.gcu.cst323.inventory.model.Product;
import com.gcu.cst323.inventory.repository.CustomerRepository;
import com.gcu.cst323.inventory.repository.ProductRepository;
import com.gcu.cst323.inventory.repository.UserAccountRepository;
import com.gcu.cst323.inventory.service.AuthService;
import com.gcu.cst323.inventory.service.OrderService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserAccountRepository userAccountRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final AuthService authService;

    public DataInitializer(UserAccountRepository userAccountRepository,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository,
                           OrderService orderService,
                           AuthService authService) {
        this.userAccountRepository = userAccountRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.authService = authService;
    }

    @Override
    public void run(String... args) {
        if (userAccountRepository.findByUsername("admin").isEmpty()) {
            authService.register("admin", "admin123", "ADMIN");
        }

        if (customerRepository.count() > 0 || productRepository.count() > 0) {
            return;
        }

        Customer jane = customerRepository.save(
                new Customer("Jane", "Smith", "jane.smith@example.com", "555-0100", "100 Main Street")
        );

        customerRepository.save(
                new Customer("Carlos", "Rivera", "carlos.rivera@example.com", "555-0110", "245 Oak Avenue")
        );

        Product laptopStand = productRepository.save(
                new Product("Laptop Stand", "Adjustable aluminum stand", new BigDecimal("39.99"), 20, 5, true)
        );

        productRepository.save(
                new Product("USB-C Hub", "Multi-port adapter", new BigDecimal("49.99"), 8, 10, true)
        );

        productRepository.save(
                new Product("Wireless Keyboard", "Bluetooth keyboard", new BigDecimal("59.99"), 16, 6, true)
        );

        orderService.createOrder(jane.getCustomerId(), laptopStand.getProductId(), 2);
    }
}