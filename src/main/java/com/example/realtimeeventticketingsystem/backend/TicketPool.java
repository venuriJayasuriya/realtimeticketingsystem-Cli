package com.example.realtimeeventticketingsystem.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private static final Logger logger = LogManager.getLogger(TicketPool.class);

    private final List<Integer> tickets; // List to store ticket IDs
    private int maxTicketCapacity;
    private int currentTicketCount;  // Keeps track of the next ticket number to assign
    private int ticketIdCounter; // Counter to ensure unique ticket IDs
    private volatile boolean soldOut = false; // Signal flag for thread termination
    private boolean isMaxCapacityReached = false;

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxTicketCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.currentTicketCount = totalTickets;  // Start ticket ID at 1
        this.ticketIdCounter = totalTickets + 1;// Start from the next ticket ID after the initial tickets

        // Add the initial tickets to the pool
        for (int i = 1; i <= totalTickets; i++) {
            tickets.add(i);
        }
    }

    // Method to add tickets to the pool
    public synchronized void addTickets(int VendorId) {
        if (currentTicketCount < maxTicketCapacity) {
            int remainingCapacity = maxTicketCapacity - currentTicketCount;
            if (remainingCapacity > 0) {
                int ticket = ticketIdCounter++;
                tickets.add(ticket);// Add the ticket to the pool
                currentTicketCount++;   // Increment the total ticket count
                logger.info("Ticket {} added by vendor {}", ticket, VendorId);
                notifyAll(); // Notify customers that tickets are available
            }
        } else if (!isMaxCapacityReached) {

            System.out.println("\n" + "----------------------------------------");
            logger.warn("The ticket pool has reached maximum capacity.");
            System.out.println("----------------------------------------\n");
            isMaxCapacityReached = true; // Set flag to prevent duplicate messages
        }
    }

    // Method to remove a ticket from the pool
    public synchronized Integer removeTicket(int CustomerId) {
        while (tickets.isEmpty()) {
            try {
                logger.info("No tickets available. Customer {} is waiting...",CustomerId);
                wait();  // Wait until tickets are available
            } catch (InterruptedException e) {
                // Handle the interruption properly
                Thread.currentThread().interrupt();  // Re-interrupt the current thread
                logger.error("Customer thread interrupted: {}" , e.getMessage());
                return null;  // Return null or handle as needed
            }
        }

        if (soldOut) {
            return null;
        }

        Integer ticket = tickets.remove(0);
        logger.info("Ticket {} sold to customer {}", ticket, CustomerId);
        return ticket;
    }

    // Method to check if all tickets have been sold
    public synchronized boolean isTicketSoldOut() {
        if (currentTicketCount >= maxTicketCapacity && tickets.isEmpty()) {
            if (!soldOut) { // Print the message only once
                soldOut = true;
                System.out.println("\n" + "----------------------------------------");
                logger.info("All tickets are sold out.");
                System.out.println("----------------------------------------");
                return true;
            }
            return true;
        }
        return false;
    }

    // Reset method to prepare for a new event
    public void reset(int maxCapacity, int totalTickets) {
        this.maxTicketCapacity = maxCapacity;
        this.tickets.clear();
        this.currentTicketCount = totalTickets;
        this.ticketIdCounter = totalTickets + 1;
        this.soldOut = false;
        this.isMaxCapacityReached = false;

        // Add the initial tickets to the pool
        for (int i = 1; i <= totalTickets; i++) {
            tickets.add(i);
        }
    }
        public boolean isSoldOut() {
            return soldOut;
        }

}
