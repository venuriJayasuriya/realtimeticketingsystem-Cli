package com.example.realtimeeventticketingsystem;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class SystemCLI {
    private SystemConfig config;
    private TicketPool ticketPool;
    private List<Thread> vendorThreads;
    private List<Thread> customerThreads;
    private AtomicBoolean isRunning;

    public SystemCLI() {
        this.config = new SystemConfig();
        this.vendorThreads = new ArrayList<>();
        this.customerThreads = new ArrayList<>();
        this.isRunning = new AtomicBoolean(false);
    }

    public SystemCLI(SystemConfig config, TicketPool ticketPool) {
        this.config = config;
        this.ticketPool = ticketPool;
        this.vendorThreads = new ArrayList<>();
        this.customerThreads = new ArrayList<>();
        this.isRunning = new AtomicBoolean(false);
    }

    public SystemConfig prompts() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the max number of ticket capacity for the event:");
        config.setMaxTicketCapacity(intValidator(scanner));
        //to check if the user enters a greater value for total tickets than the maximum capacity
        while (true) {
            System.out.println("Please enter the total number of tickets already released:");
            int totalTickets = intValidator(scanner);
            if (totalTickets <= config.getMaxTicketCapacity()) {
                config.setTotalTickets(totalTickets);
                break;
            } else {
                System.out.println("Error: Total tickets cannot exceed the maximum ticket capacity. Please try again.");
            }
        }
        //to check the number of vendors selling tickets for the event
        while (true) {
            System.out.println("Please enter the number of ticket vendors (minimum 1, maximum 5):");
            int vendorCount = intValidator(scanner);
            if (vendorCount >= 1 && vendorCount <= 5) {
                config.setVendorCount(vendorCount);
                break;
            } else {
                System.out.println("Vendor count must be between 1 and 5. Please try again.");
            }
        }
        System.out.println("Please enter the number of tickets released by vendors per second:");
        config.setTicketReleaseRate(intValidator(scanner));
        System.out.println("Please enter the number of tickets retrieved by customers per second:");
        config.setCustomerRetrievalRate(intValidator(scanner));

        System.out.println();

        return config;
    }

    //method to save and load ticket information to JSON
    public void saveAndLoadToJson() {
        config.saveToJson();

        System.out.println();

        SystemConfig loadedTicketInfo = config.loadFromJson();
        if (loadedTicketInfo != null) {
            System.out.println("Loaded ticketing information from JSON:");
            System.out.println(loadedTicketInfo);
        }
    }

    //method to validate the inputs taken
    private static int intValidator(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= 0) {
                    return input;
                } else {
                    System.out.println("Please enter a positive integer");
                }
            } else {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }
    }

    public void start() {
        // Reset threads and running state before starting
        vendorThreads.clear();
        customerThreads.clear();

        if (isRunning.compareAndSet(false, true)) {
            System.out.println("\n" + "----------------------------------------");
            System.out.println("Starting ticket handling system...");
            System.out.println("----------------------------------------");

            // Create and start vendor threads
            for (int i = 1; i <= config.getVendorCount(); i++) {
                Thread vendor = new Thread(new Vendor(ticketPool, config.getTicketReleaseRate(), i));
                vendorThreads.add(vendor);
                vendor.start();
            }

            // Create and start customer threads
            for (int i = 1; i <= 2; i++) {
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
            System.out.println("System is already running.");
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
            System.out.println("Ticket handling system stopped.");
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
            System.out.println("System control was interrupted.");
        }
    }
}
