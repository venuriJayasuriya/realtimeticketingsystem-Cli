package com.example.realtimeeventticketingsystem.worker;

import com.example.realtimeeventticketingsystem.model.TicketPool;
import org.springframework.stereotype.Component;

@Component
public class Vendor implements Runnable {
    private TicketPool ticketPool;
    private int ticketReleaseRate;
    private int vendorId;

    // Default constructor for Spring
    public Vendor() {}

    public void initialize(TicketPool ticketPool, int ticketReleaseRate, int vendorId) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (ticketPool) {
                    if (ticketPool.isTicketSoldOut())
                        break;
                    ticketPool.addTickets(vendorId);  // Add a ticket to the pool
                }
                Thread.sleep(1000/ticketReleaseRate); // Convert seconds to milliseconds here
            }
        } catch (InterruptedException e) {
            // Expected interruption, just exit the thread
            Thread.currentThread().interrupt();
        }
    }
}
