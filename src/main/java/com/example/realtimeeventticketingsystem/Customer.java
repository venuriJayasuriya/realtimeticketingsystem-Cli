package com.example.realtimeeventticketingsystem;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        while (true) {
            ticketPool.removeTicket();
            try {
                Thread.sleep(customerRetrievalRate * 1000); // Convert seconds to milliseconds here
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer thread interrupted: " + e.getMessage());
                break;
            }
        }
    }
}
