package com.example.realtimeeventticketingsystem;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to Real-time Event Ticketing System!");
        SystemCLI Cli = new SystemCLI();
        SystemConfig config = Cli.prompts();//called the prompt method to gather information on tickets save and load to JSON
        Cli.saveAndLoadToJson();//Save and load ticket information to JSON


        // Initialize the TicketPool with max ticket capacity
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(),config.getTotalTickets());

        // Create and start vendor threads
        Thread vendor1 = new Thread(new Vendor(ticketPool, config.getTicketReleaseRate(), 1));
        Thread vendor2 = new Thread(new Vendor(ticketPool, config.getTicketReleaseRate(), 2));
        Thread vendor3 = new Thread(new Vendor(ticketPool, config.getTicketReleaseRate(), 3));

        // Create and start customer threads
        Thread customer1 = new Thread(new Customer(ticketPool, config.getCustomerRetrievalRate(), 1));
        Thread customer2 = new Thread(new Customer(ticketPool, config.getCustomerRetrievalRate(), 2));

        vendor1.start();
        vendor2.start();
        vendor3.start();
        customer1.start();
        customer2.start();

    }
}
