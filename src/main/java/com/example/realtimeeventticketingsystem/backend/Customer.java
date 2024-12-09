package com.example.realtimeeventticketingsystem.backend;

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
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (ticketPool) {
                    if (ticketPool.isTicketSoldOut() && ticketPool.isSoldOut())
                        break;

                    Integer ticket = ticketPool.removeTicket(customerId);
                    if (ticket == null) {
                        if (ticketPool.isSoldOut())
                            break;
                        ticketPool.wait(); // Wait for new tickets or sold-out notification
                    }
                }
                Thread.sleep(1000/customerRetrievalRate);
            }
        } catch (InterruptedException e) {
            // Expected interruption, just exit the thread
            Thread.currentThread().interrupt();
        }
    }
}
