package com.example.realtimeeventticketingsystem;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Integer> tickets; // List to store ticket IDs
    private final int maxTicketCapacity;

    public TicketPool(int maxCapacity) {
        this.maxTicketCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>());
    }

    // Method to add tickets to the pool
    public synchronized void addTickets(int ticketId) {
        if (tickets.size() < maxTicketCapacity) {
            tickets.add(ticketId);
            System.out.println("Ticket " + ticketId + " added to the pool.");
            notifyAll(); // Notify customers that tickets are available
        } else {
            System.out.println("Pool is full. Cannot add ticket " + ticketId);
        }
    }

    // Method to remove a ticket from the pool
    public synchronized Integer removeTicket() {
        while (tickets.isEmpty()) {
            try {
                System.out.println("No tickets available. Customer is waiting...");
                wait(); // Wait until tickets are available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }
        Integer ticket = tickets.remove(0);
        System.out.println("Ticket " + ticket + " sold to a customer.");
        return ticket;
    }

    public int getCurrentSize() {
        return tickets.size();
    }
}
