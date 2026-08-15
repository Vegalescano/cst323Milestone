package com.gcu.cst323.inventory.controller;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health endpoint for Milestone 4 availability monitoring.
 * REST controller provides the public health endpoint.
 * The endpoint is used by UptimeRobot to monitor cloud application availability.
 */
@RestController
public class HealthController {
	/**
	 * Returns a simple status message confirming that the Azure application is running.
	 *
	 * @return health status message with the current server time
	 */
    @GetMapping("/health")
    public String health() {
        return "CST323Inventory is running on Azure. Time: " + LocalDateTime.now();
    }
}