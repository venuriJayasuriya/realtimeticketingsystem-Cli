package com.example.realtimeeventticketingsystem;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final int maxTickets;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int maxTickets) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.maxTickets = maxTickets;
    }

    @Override
    public void run() {
        int ticketId = 1;
        while (ticketId <= maxTickets) {
            ticketPool.addTickets(ticketId);
            ticketId++;
            try {
                Thread.sleep(ticketReleaseRate * 1000); // Convert seconds to milliseconds here
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor thread interrupted: " + e.getMessage());
            }
        }
        System.out.println(Thread.currentThread().getName() + " has finished releasing tickets.");
    }
}
