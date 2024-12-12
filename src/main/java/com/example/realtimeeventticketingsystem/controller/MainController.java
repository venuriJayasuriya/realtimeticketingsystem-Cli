package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.model.TicketPool;
import com.example.realtimeeventticketingsystem.model.TicketPoolStatus;
import com.example.realtimeeventticketingsystem.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {
    private final SystemConfig systemConfig;
    private final TicketPool ticketPool;
    private final SystemController systemController;

    @Autowired
    public MainController(SystemConfig systemConfig, TicketPool ticketPool, SystemController systemController) {
        this.systemConfig = systemConfig;
        this.ticketPool = ticketPool;
        this.systemController = systemController;
    }

    @GetMapping("/tickets/status")
    public ResponseEntity<TicketPoolStatus> getStatus() {
        TicketPoolStatus status = new TicketPoolStatus(
                ticketPool.getCurrentTicketCount(),
                ticketPool.getMaxTicketCapacity(),
                ticketPool.isSoldOut(),
                ticketPool.getAvailableTickets(),  // Use the method to get available tickets
                ticketPool.getTotalSold()          // Use the method to get total sold
        );
        return ResponseEntity.ok(status);
    }

    @PostMapping("/system/initialize")
    public ResponseEntity<?> initializeSystem(@RequestBody Map<String, Integer> config) {
        try {
            systemConfig.setMaxTicketCapacity(config.get("maxTicketCapacity"));
            systemConfig.setTotalTickets(config.get("totalTickets"));
            systemConfig.setVendorCount(config.get("vendorCount"));
            systemConfig.setTicketReleaseRate(config.get("ticketReleaseRate"));
            systemConfig.setCustomerRetrievalRate(config.get("customerRetrievalRate"));

            systemConfig.saveToJson();
            ticketPool.reset(systemConfig.getMaxTicketCapacity(), systemConfig.getTotalTickets());

            return ResponseEntity.ok(Map.of("message", "System initialized successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/system/start")
    public ResponseEntity<?> startSystem() {
        try {
            systemController.start();
            return ResponseEntity.ok(Map.of("message", "System started successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/system/stop")
    public ResponseEntity<?> stopSystem() {
        try {
            systemController.stop();
            return ResponseEntity.ok(Map.of("message", "System stopped successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/system/validate")
    public ResponseEntity<?> validateConfig(@RequestBody Map<String, Integer> config) {
        Map<String, String> errors = new HashMap<>();

        // Validate maxTicketCapacity
        int maxTicketCapacity = config.get("maxTicketCapacity");
        if (maxTicketCapacity <= 0) {
            errors.put("maxTicketCapacity", "Please enter a positive integer");
        }

        // Validate totalTickets
        int totalTickets = config.get("totalTickets");
        if (totalTickets <= 0) {
            errors.put("totalTickets", "Please enter a positive integer");
        } else if (totalTickets > maxTicketCapacity) {
            errors.put("totalTickets", "Total tickets cannot exceed the maximum ticket capacity");
        }

        // Validate vendorCount
        int vendorCount = config.get("vendorCount");
        if (vendorCount < 1 || vendorCount > 5) {
            errors.put("vendorCount", "Vendor count must be between 1 and 5");
        }

        // Validate rates
        if (config.get("ticketReleaseRate") <= 0) {
            errors.put("ticketReleaseRate", "Please enter a positive integer");
        }
        if (config.get("customerRetrievalRate") <= 0) {
            errors.put("customerRetrievalRate", "Please enter a positive integer");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok().build();
    }

}