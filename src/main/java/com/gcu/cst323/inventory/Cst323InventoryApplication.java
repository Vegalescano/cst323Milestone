package com.gcu.cst323.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Main entry point for the CST323Inventory Spring Boot application.
 * Starts the web application, loads Spring components, and runs the
 * inventory and order management system.
 */
public class Cst323InventoryApplication {
	/**
	 * Starts the Spring Boot application.
	 * @param args command line arguments passed to the application
	 */
    public static void main(String[] args) {
        SpringApplication.run(Cst323InventoryApplication.class, args);
    }
}