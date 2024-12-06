package com.example.realtimeeventticketingsystem;

import java.util.Scanner;

public class SystemCLI {

    private SystemConfig config;

    public SystemCLI() {
        this.config = new SystemConfig();
    }

    public SystemConfig prompts() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the max number of ticket capacity for the event:");
        config.setMaxTicketCapacity(intValidator(scanner));
        //to check if the user enters a greater value for total tickets than the maximum capacity
        while (true) {
            System.out.println("Please enter the total number of tickets for the event:");
            int totalTickets = intValidator(scanner);
            if (totalTickets <= config.getMaxTicketCapacity()) {
                config.setTotalTickets(totalTickets);
                break;
            } else {
                System.out.println("Error: Total tickets cannot exceed the maximum ticket capacity. Please try again.");
            }
        }
        System.out.println("Please enter the rate of ticket release for the event:");
        config.setTicketReleaseRate(intValidator(scanner));
        System.out.println("Please enter the customer retrieval rate for the event:");
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
        while(true){
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= 0) {
                    return input;
                } else {
                    System.out.println("Please enter a positive integer");
                }
            }
            else {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }
    }

}
