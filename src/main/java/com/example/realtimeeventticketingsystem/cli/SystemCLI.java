package com.example.realtimeeventticketingsystem.cli;

import com.example.realtimeeventticketingsystem.backend.SystemConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

public class SystemCLI {
    private static final Logger logger = LogManager.getLogger(SystemCLI.class);
    private final SystemConfig config;

    public SystemCLI() {
        this.config = new SystemConfig();
    }

    public SystemConfig prompts() {
        Scanner scanner = new Scanner(System.in);

        logger.info("Please enter the max number of ticket capacity for the event:");
        config.setMaxTicketCapacity(intValidator(scanner));
        //to check if the user enters a greater value for total tickets than the maximum capacity
        while (true) {
            logger.info("Please enter the total number of tickets already released:");
            int totalTickets = intValidator(scanner);
            if (totalTickets <= config.getMaxTicketCapacity()) {
                config.setTotalTickets(totalTickets);
                break;
            } else {
                logger.warn("Warn: Total tickets cannot exceed the maximum ticket capacity. Please try again.");
            }
        }
        //to check the number of vendors selling tickets for the event
        while (true) {
            logger.info("Please enter the number of ticket vendors (minimum 1, maximum 5):");
            int vendorCount = intValidator(scanner);
            if (vendorCount >= 1 && vendorCount <= 5) {
                config.setVendorCount(vendorCount);
                break;
            } else {
                logger.warn("Warn: Vendor count must be between 1 and 5. Please try again.");
            }
        }
        logger.info("Please enter the number of tickets released by vendors per second:");
        config.setTicketReleaseRate(intValidator(scanner));
        logger.info("Please enter the number of tickets retrieved by customers per second:");
        config.setCustomerRetrievalRate(intValidator(scanner));

        System.out.println();

        return config;
    }


    //method to validate the inputs taken
    private static int intValidator(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= 0) {
                    return input;
                } else {
                    logger.warn("Please enter a positive integer");
                }
            } else {
                logger.warn("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }
    }
}
