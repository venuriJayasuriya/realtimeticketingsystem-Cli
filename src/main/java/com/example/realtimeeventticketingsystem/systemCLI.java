package com.example.realtimeeventticketingsystem;

import java.util.Scanner;

public class systemCLI {

    private systemConfig config;

    public systemCLI() {

    }

    public void prompts() {
        Scanner scanner = new Scanner(System.in);

        systemConfig config = new systemConfig();

        System.out.println("Please enter the total number of tickets for the event:");
        config.setTotalTickets(intValidator(scanner));
        System.out.println("Please enter the rate of ticket release for the event:");
        config.setTicketReleaseRate(doubleValidator(scanner));
        System.out.println("Please enter the customer retrieval rate for the event:");
        config.setCustomerRetrievalRate(doubleValidator(scanner));
        System.out.println("Please enter the max number of ticket capacity for the event:");
        config.setMaxTicketCapacity(intValidator(scanner));

        System.out.println();
        System.out.println(config);

        config.saveToJson();

        systemConfig loadedTicketInfo = config.loadFromJson();
        if (loadedTicketInfo != null) {
            System.out.println("Loaded ticketing information from JSON:");
            System.out.println(loadedTicketInfo);
        }
    }

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

    private static double doubleValidator(Scanner scanner) {
        while(true){
            if (scanner.hasNextDouble()) {
                double input = scanner.nextDouble();
                if (input > 0) {
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
