package com.example.realtimeeventticketingsystem;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private final int customerId;

    public Customer(TicketPool ticketPool, int customerRetrievalRate, int customerId) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        while (!ticketPool.isTicketSoldOut()) {
            Integer ticket = ticketPool.removeTicket(customerId); // Try to remove a ticket
            try {
                Thread.sleep(customerRetrievalRate * 1000); // Convert seconds to milliseconds here
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer thread interrupted: " + e.getMessage());
            }
        }
        System.out.println("Customer " + customerId + " finished purchasing tickets.");
    }
}
