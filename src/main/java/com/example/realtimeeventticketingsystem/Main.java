package com.example.realtimeeventticketingsystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to Real-time Event Ticketing System!");
            System.out.println("===========================================================\n");

            SystemCLI Cli = new SystemCLI();
            SystemConfig config = Cli.prompts();//called the prompt method to gather information on tickets save and load to JSON
            Cli.saveAndLoadToJson();//Save and load ticket information to JSON

            // Initialize the TicketPool with max ticket capacity
            TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

            // Create SystemController
            SystemCLI systemController = new SystemCLI(config, ticketPool);

            // Start interactive control
            systemController.startInteractiveControl();

            System.out.println("\n" + "----------------------------------------");
            System.out.println("What would you like to do?");
            System.out.println("1. Start a new event");
            System.out.println("2. Exit the system");
            System.out.print("Enter your choice (1-2): ");

            String choice = scanner.nextLine().trim();

            if (!choice.equals("1")) {
                System.out.println("\n" + "===========================================================");
                System.out.println("Thank you for using the Real-time Event Ticketing System. \nGoodbye!");
                System.out.println("===========================================================\n");
                break;
            }

            // Reset the ticket pool for the new event
            ticketPool.reset(config.getMaxTicketCapacity(), config.getTotalTickets());
        }
        // Close the scanner
        scanner.close();
    }
}
