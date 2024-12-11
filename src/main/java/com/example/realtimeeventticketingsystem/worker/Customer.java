package com.example.realtimeeventticketingsystem.worker;

import com.example.realtimeeventticketingsystem.model.TicketPool;
import org.springframework.stereotype.Component;

@Component
public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int customerRetrievalRate;
    private int customerId;

    public Customer() {}

    public void initialize(TicketPool ticketPool, int customerRetrievalRate, int customerId) {
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
