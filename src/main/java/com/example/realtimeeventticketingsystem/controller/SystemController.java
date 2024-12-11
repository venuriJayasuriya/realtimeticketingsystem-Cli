package com.example.realtimeeventticketingsystem.controller;

import com.example.realtimeeventticketingsystem.config.SystemConfig;
import com.example.realtimeeventticketingsystem.model.TicketPool;
import com.example.realtimeeventticketingsystem.worker.Customer;
import com.example.realtimeeventticketingsystem.worker.Vendor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class SystemController {
    private static final Logger logger = LogManager.getLogger(SystemController.class);
    private final TicketPool ticketPool;
    private final SystemConfig config;
    private final List<Thread> vendorThreads;
    private final List<Thread> customerThreads;
    private final AtomicBoolean isRunning;
    private final ApplicationContext applicationContext;


    @Autowired
    public SystemController(SystemConfig config, TicketPool ticketPool, ApplicationContext applicationContext) {
        this.config = config;
        this.ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
        this.vendorThreads = new ArrayList<>();
        this.customerThreads = new ArrayList<>();
        this.isRunning = new AtomicBoolean(false);
        this.applicationContext = applicationContext;
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
                Vendor vendor = applicationContext.getBean(Vendor.class);
                vendor.initialize(ticketPool, config.getTicketReleaseRate(), i);
                Thread vendorThread = new Thread(vendor);
                vendorThreads.add(vendorThread);
                vendorThread.start();
            }

            // Generate random number of customers (1 to 10)
            int customerCount = new Random().nextInt(10) + 1; // 1 to 10
            // Create and start customer threads
            for (int i = 1; i <= customerCount; i++) {
                Customer customer = applicationContext.getBean(Customer.class);
                customer.initialize(ticketPool, config.getCustomerRetrievalRate(), i);
                Thread customerThread = new Thread(customer);
                customerThreads.add(customerThread);
                customerThread.start();
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

            System.out.println("\n" + "----------------------------------------");
            logger.info("Ticket handling system stopped.");
            System.out.println("----------------------------------------");

            // Clear the threads lists
            vendorThreads.clear();
            customerThreads.clear();

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
