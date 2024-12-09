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
