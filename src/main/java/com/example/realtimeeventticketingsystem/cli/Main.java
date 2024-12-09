package com.example.realtimeeventticketingsystem.cli;

import com.example.realtimeeventticketingsystem.backend.SystemConfig;
import com.example.realtimeeventticketingsystem.backend.SystemController;
import com.example.realtimeeventticketingsystem.backend.TicketPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Logger logger = LogManager.getLogger();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===========================================================");
            logger.info("Welcome to Real-time Event Ticketing System!");
            System.out.println("===========================================================\n");

            SystemCLI Cli = new SystemCLI();
            SystemConfig config = Cli.prompts();//called the prompt method to gather information on tickets save and load to JSON
            config.saveAndLoadToJson();//Save and load ticket information to JSON

            // Initialize the TicketPool with max ticket capacity
            TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

            // Create SystemController
            SystemController systemController = new SystemController(config, ticketPool);

            // Start interactive control
            systemController.startInteractiveControl();

            System.out.println("\n" + "----------------------------------------");
            logger.info("What would you like to do?");
            logger.info("1. Start a new event");
            logger.info("2. Exit the system");
            logger.info("Enter your choice (1-2): ");

            String choice = scanner.nextLine().trim();

            if (!choice.equals("1")) {
                System.out.println("\n" + "===========================================================");
                logger.info("Thank you for using the Real-time Event Ticketing System. \nGoodbye!");
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
