package com.example.realtimeeventticketingsystem.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class SystemController {
    private static final Logger logger = LogManager.getLogger(SystemController.class);
    private final TicketPool ticketPool;
    private final SystemConfig config;
    private final List<Thread> vendorThreads;
    private final List<Thread> customerThreads;
    private final AtomicBoolean isRunning;

    public SystemController(SystemConfig config, TicketPool ticketPool) {
        this.config = config;
        this.ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
        this.vendorThreads = new ArrayList<>();
        this.customerThreads = new ArrayList<>();
        this.isRunning = new AtomicBoolean(false);
    }

    public void start() {
        // Reset threads and running state before starting
        vendorThreads.clear();
        customerThreads.clear();

        if (isRunning.compareAndSet(false, true)) {
            System.out.println("\n" + "----------------------------------------");
            logger.info("Starting ticket handling system...");
            System.out.println("----------------------------------------");

            // Create and start vendor threads
            for (int i = 1; i <= config.getVendorCount(); i++) {
                Thread vendor = new Thread(new Vendor(ticketPool, config.getTicketReleaseRate(), i));
                vendorThreads.add(vendor);
                vendor.start();
            }

            // Generate random number of customers (1 to 10)
            int customerCount = new Random().nextInt(10) + 1; // 1 to 10
            // Create and start customer threads
            for (int i = 1; i <= customerCount; i++) {
                Thread customer = new Thread(new Customer(ticketPool, config.getCustomerRetrievalRate(), i));
                customerThreads.add(customer);
                customer.start();
            }

            // Monitor thread to check for ticket sell-out
            Thread monitorThread = new Thread(() -> {
                while (!ticketPool.isTicketSoldOut() && isRunning.get()) {
                    try {
                        Thread.sleep(1000); // Check every second
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (ticketPool.isTicketSoldOut()) {
                    stop();
                }
            });
            monitorThread.start();
        } else {
            logger.info("System is already running.");
        }
    }

    public void stop() {
        if (isRunning.compareAndSet(true, false)) {

            // Interrupt all vendor threads
            for (Thread vendor : vendorThreads) {
                vendor.interrupt();
            }

            // Interrupt all customer threads
            for (Thread customer : customerThreads) {
                customer.interrupt();
            }

            // Wait for threads to terminate
            for (Thread vendor : vendorThreads) {
                try {
                    vendor.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            for (Thread customer : customerThreads) {
                try {
                    customer.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Clear the threads lists
            vendorThreads.clear();
            customerThreads.clear();

            System.out.println("\n" + "----------------------------------------");
            logger.info("Ticket handling system stopped.");
            System.out.println("----------------------------------------");

        } else {
            System.out.println();
        }
    }

    public void startInteractiveControl() {
        try {
            start(); // Automatically start the system
            // Wait for the system to complete (tickets sold out)
            while (!ticketPool.isTicketSoldOut()) {
                Thread.sleep(1000);
            }
            stop();// Stop the system automatically when sold out

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("System control was interrupted.");
        }
    }
}
