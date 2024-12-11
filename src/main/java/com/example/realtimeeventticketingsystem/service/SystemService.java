package com.example.realtimeeventticketingsystem.service;

import com.example.realtimeeventticketingsystem.config.SystemConfig;
import com.example.realtimeeventticketingsystem.model.TicketPool;
import org.springframework.stereotype.Service;

@Service
public class SystemService {
    private final SystemConfig systemConfig;
    private final TicketPool ticketPool;

    public SystemService(SystemConfig systemConfig, TicketPool ticketPool) {
        this.systemConfig = systemConfig;
        this.ticketPool = ticketPool;
    }

    public void initialize(int maxCapacity, int totalTickets, int vendorCount,
                           int ticketReleaseRate, int customerRetrievalRate) {
        systemConfig.setMaxTicketCapacity(maxCapacity);
        systemConfig.setTotalTickets(totalTickets);
        systemConfig.setVendorCount(vendorCount);
        systemConfig.setTicketReleaseRate(ticketReleaseRate);
        systemConfig.setCustomerRetrievalRate(customerRetrievalRate);

        ticketPool.reset(maxCapacity, totalTickets);
        systemConfig.saveToJson();
    }
}