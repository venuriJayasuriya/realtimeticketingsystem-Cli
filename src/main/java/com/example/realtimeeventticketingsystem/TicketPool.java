package com.example.realtimeeventticketingsystem;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Integer> tickets; // List to store ticket IDs
    private final int maxTicketCapacity;
    private int currentTicketCount;;  // Keeps track of the next ticket number to assign
    private int ticketIdCounter; // Counter to ensure unique ticket IDs

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxTicketCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.currentTicketCount = totalTickets;  // Start ticket ID at 1
        this.ticketIdCounter = totalTickets + 1; // Start from the next ticket ID after the initial tickets

        // Add the initial tickets to the pool
        for (int i = 1; i <= totalTickets; i++) {
            tickets.add(i);
        }
        System.out.println();
        System.out.println("Initial tickets loaded: " + totalTickets);
    }

    // Method to add tickets to the pool
    public synchronized void addTickets(int VendorId) {
        if (currentTicketCount < maxTicketCapacity) {
            int remainingCapacity = maxTicketCapacity - currentTicketCount;
            if (remainingCapacity > 0) {
                int ticket = ticketIdCounter++;
                tickets.add(ticket);// Add the ticket to the pool
                currentTicketCount++;   // Increment the total ticket count
                System.out.println("Ticket " + ticket + " added by vendor " + VendorId);
                notifyAll(); // Notify customers that tickets are available
            }
        } else {
            if(currentTicketCount == maxTicketCapacity ) {
                System.out.println("Ticket pool has reached maximum capacity.");
            }
        }
    }

    // Method to remove a ticket from the pool
    public synchronized Integer removeTicket(int CustomerId) {
        while (tickets.isEmpty()) {
            try {
                System.out.println("No tickets available. Customer " + CustomerId + " is waiting...");
                wait();  // Wait until tickets are available
            } catch (InterruptedException e) {
                // Handle the interruption properly
                Thread.currentThread().interrupt();  // Re-interrupt the current thread
                System.out.println("Customer thread interrupted: " + e.getMessage());
                return null;  // Return null or handle as needed
            }
        }

        Integer ticket = tickets.remove(0);
        System.out.println("Ticket " + ticket + " sold to customer " + CustomerId);
        return ticket;
    }

//    public int getCurrentSize() {
//        return tickets.size();
//    }
//    public int getCurrentTicketCount() {
//        return currentTicketCount;
//    }
//
//    public int getMaxTicketCapacity() {
//        return maxTicketCapacity;
//    }

    // Method to check if all tickets have been sold
    public synchronized boolean isTicketSoldOut() {
        return currentTicketCount >= maxTicketCapacity && tickets.isEmpty();
    }

}
