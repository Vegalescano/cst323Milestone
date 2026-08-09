package com.gcu.cst323.inventory.controller;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health endpoint for Milestone 4 availability monitoring.
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "CST323Inventory is running on Azure. Time: " + LocalDateTime.now();
    }
}