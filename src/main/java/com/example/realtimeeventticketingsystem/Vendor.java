package com.example.realtimeeventticketingsystem;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final int vendorId;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int vendorId) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        while (!ticketPool.isTicketSoldOut()) {
            ticketPool.addTickets(vendorId);  // Add a ticket to the pool
            try {
                Thread.sleep(ticketReleaseRate * 1000); // Convert seconds to milliseconds here
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor thread interrupted: " + e.getMessage());
            }
        }
        System.out.println("Vendor " + vendorId + " has finished releasing tickets.");
    }
}
